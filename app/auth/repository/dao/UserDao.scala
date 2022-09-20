package auth.repository.dao

import auth.repository.po.{UserPo, UserTable}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig}
import slick.jdbc.PostgresProfile
import slick.jdbc.PostgresProfile.api._
import slick.lifted.TableQuery

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserDao @Inject()(dbConfigProvider: DatabaseConfigProvider)
                       (implicit ec: ExecutionContext) extends HasDatabaseConfig[PostgresProfile] {

  override protected val dbConfig = dbConfigProvider.get[PostgresProfile]

  private val users = TableQuery[UserTable]

  def list(): Future[Seq[UserPo]] = {
    db.run(users.result)
  }

  def findByUsername(username: String): Future[Option[UserPo]] = {
    db.run(users.filter(_.username === username).result.headOption)
  }


}
