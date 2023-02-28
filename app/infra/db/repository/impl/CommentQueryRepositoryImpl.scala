package infra.db.repository.impl

import common.{Page, PageQuery}
import infra.db.po.CommentsPo
import infra.db.po.CommentsPo.CommentTable
import infra.db.repository.CommentQueryRepository
import interfaces.dto.{ArticlePageQuery, CommentPageQuery}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig}
import slick.basic.DatabaseConfig
import slick.jdbc.{GetResult, PostgresProfile, SQLActionBuilder}
import slick.jdbc.PostgresProfile.api._
import slick.sql.SqlAction

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CommentQueryRepositoryImpl @Inject() (private val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext)
    extends CommentQueryRepository
    with HasDatabaseConfig[PostgresProfile] {

  override protected val dbConfig: DatabaseConfig[PostgresProfile] = dbConfigProvider.get[PostgresProfile]

  private val comments = TableQuery[CommentTable]

  override def get(id: Long): Future[Option[CommentsPo]] = ???

  override def list(): Future[Seq[CommentsPo]] = ???

  override def count(): Future[Int] = ???

  override def listByPage(pageQuery: PageQuery): Future[Page[CommentsPo]] = {
    def doQuery(query: CommentPageQuery): Future[Page[CommentsPo]] = {
      val finalQuery = comments
        .filterOpt(query.resourceId)(_.resourceId === _)
        .filterOpt(query.typ)(_.typ === _)
        .filterOpt(query.parent)(_.replyTo === _)

      db.run {
        for {
          pageResult <- finalQuery.drop(query.offset).take(query.limit).sorted(_.createAt.asc).result
          count      <- finalQuery.length.result
        } yield Page(query.page, query.size, count, pageResult)
      }
    }

    pageQuery match {
      case query: CommentPageQuery => doQuery(query)
      case _                       => Future.successful(Page.empty())
    }
  }

  /** 批量查询评论下面的回复，可以限制 group by top N order by creat_at
   *  @param rootCommentIds
   *    父评论的id 集合
   *  @param limit
   *    每一个父评论下面子评论数量的限制
   *  @return
   *    按照父评论分组的回复列表集合
   */
  def listReplyWithLimit(rootCommentIds: Seq[Long], limit: Option[Int]): Future[Map[Long, Seq[CommentsPo]]] = {
    if (rootCommentIds.isEmpty) {
      return Future.successful(Map.empty)
    }
    val limitSql = limit.map(l => s"where row <= $l").getOrElse("")
    val replyTo  = rootCommentIds.mkString(",")
    val sqlAction =
      sql"""
          with cte as (
              select id,
                     row_number() over (partition by reply_to order by create_at ) as row
              from comments where reply_to in (#$replyTo)
          )
          select id from cte #$limitSql;
         """.as[Long]

    db.run {
      for {
        replyIds <- sqlAction
        pos      <- comments.filter(_.id inSet replyIds).sorted(_.createAt).result
      } yield pos.groupBy(_.replyTo)
    }
  }

  override def listReplyByPage(pageQuery: PageQuery, parent: Long): Future[Page[CommentsPo]] = {
    // 构建 sql
    def buildQuery(select: String, page: Option[PageQuery] = None) = {
      val limitOffset = page.map(p => s"limit ${p.limit} offset ${p.offset}").getOrElse("")
      sql"""with recursive cte as (
                select id, reply_to from comments where reply_to = 1
                union
                select a.id, a.reply_to from comments as a join cte on a.reply_to = cte.id
            ) select #$select from comments where id in (select id from cte) #$limitOffset"""
    }

    db.run {
      for {
        ids        <- buildQuery("id", Some(pageQuery)).as[Long]
        pageResult <- comments.filter(_.id inSet ids).sorted(_.createAt).result
        count      <- buildQuery("count(1)", None).as[Int].head
      } yield Page(pageQuery.page, pageQuery.size, count, pageResult)
    }

  }

  override def replyCountMap(ids: Seq[Long]): Future[Map[Long, Int]] = {
    if (ids.isEmpty) {
      return Future.successful(Map.empty)
    }
    val idString = ids.mkString(",")
    val sqlAction =
      sql"""
            with recursive cte as (
                    select id, reply_to from comments where reply_to in (#$idString)
                    union
                    select a.id, a.reply_to from comments as a join cte on a.reply_to = cte.id
                ) select reply_to, count(1) from comments where id in (select id from cte)  group by reply_to
         """.as[(Long, Int)]
    db.run(sqlAction).map(vector => vector.map(tuple => tuple._1 -> tuple._2).toMap)
  }
}
