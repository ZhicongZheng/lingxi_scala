package infra.db.repository.impl

import common.Constant
import domain.comment.{Comment, CommentRepository}
import infra.db.po.CommentsPo.CommentTable
import infra.db.assembler.CommentAssembler._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig}
import slick.basic.DatabaseConfig
import slick.jdbc.PostgresProfile
import slick.jdbc.PostgresProfile.api._
import slick.lifted.TableQuery

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CommentRepositoryImpl @Inject() (private val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext)
    extends CommentRepository
    with HasDatabaseConfig[PostgresProfile] {

  override protected val dbConfig: DatabaseConfig[PostgresProfile] = dbConfigProvider.get[PostgresProfile]

  private val comments = TableQuery[CommentTable]

  override def save(comment: Comment): Future[Long] =
    comment.id match {
      case Constant.domainCreateId => db.run(comments returning comments.map(_.id) += comment)
      case _                       => db.run(comments.filter(_.id === comment.id).update(comment)).map(_.toLong)
    }

  override def get(id: Long): Future[Option[Comment]] = ???

  override def remove(id: Long): Future[Unit] = db.run(comments.filter(_.id === id).delete).map(_ => ())
}
