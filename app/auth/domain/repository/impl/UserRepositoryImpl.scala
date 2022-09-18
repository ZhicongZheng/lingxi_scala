package auth.domain.repository.impl

import auth.domain.User
import auth.domain.repository.UserRepository
import auth.domain.repository.table.UserTable
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig}
import slick.jdbc.PostgresProfile
import slick.jdbc.PostgresProfile.api._
import slick.lifted.TableQuery

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserRepositoryImpl @Inject()(dbConfigProvider: DatabaseConfigProvider)
                                  (implicit ec: ExecutionContext)
  extends UserRepository with HasDatabaseConfig[PostgresProfile] {

  override protected val dbConfig = dbConfigProvider.get[PostgresProfile]

  private val users = TableQuery[UserTable]

  override def findById(id: Long): Future[User] = ???

  override def list(): Future[Seq[User]] = db.run(users.result)

  override def findByUsername(username: String): Future[Option[User]] = {
    //db.run(users.filter((u: UserTable) => u.username.eq(username)).result.headOption)
    Future.successful[Option[User]](None)
  }

  override def create(user: User): Future[User] = ???

  override def update(user: User): Future[User] = ???

  override def delete(id: Long): Future[User] = ???


}
