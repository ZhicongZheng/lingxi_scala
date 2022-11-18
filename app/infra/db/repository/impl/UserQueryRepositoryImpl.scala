package infra.db.repository.impl

import common.{Page, PageQuery}
import infra.db.po.UserPo
import infra.db.po.UserPo.UserTable
import infra.db.repository.UserQueryRepository
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig}
import slick.jdbc.PostgresProfile
import slick.jdbc.PostgresProfile.api._
import slick.lifted.TableQuery

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserQueryRepositoryImpl @Inject() (private val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext)
    extends UserQueryRepository
    with HasDatabaseConfig[PostgresProfile] {

  override protected val dbConfig = dbConfigProvider.get[PostgresProfile]

  private val users = TableQuery[UserTable]

  override def get(id: Long): Future[Option[UserPo]] = db.run(users.filter(_.id === id).result.headOption)

  override def list(): Future[Seq[UserPo]] = db.run(users.result)

  override def count(): Future[Int] = db.run(users.size.result)

  override def listByPage(pageQuery: PageQuery): Future[Page[UserPo]] = db.run {
    for {
      userPos <- users.drop(pageQuery.offset).take(pageQuery.limit).result
      count   <- users.length.result
    } yield Page(pageQuery.page, pageQuery.size, count, userPos)
  }

  override def findByUsername(username: String): Future[Option[UserPo]] = db.run(users.filter(_.username === username).result.headOption)
}
