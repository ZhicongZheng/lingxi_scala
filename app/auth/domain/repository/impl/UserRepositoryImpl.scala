package auth.domain.repository.impl

import auth.domain.User
import auth.domain.repository.UserRepository
import auth.domain.repository.dao.UserDao
import auth.domain.repository.table.UserTable

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserRepositoryImpl @Inject()(userDao: UserDao)
                                  (implicit ec: ExecutionContext) extends UserRepository{

  override def findById(id: Long): Future[User] = ???

  override def list(): Future[Seq[User]] = {
    userDao.list().map(users => users.map(u => User.apply(u)))
  }

  override def findByUsername(username: String): Future[Option[User]] = ???

  override def create(user: User): Future[User] = ???

  override def update(user: User): Future[User] = ???

  override def delete(id: Long): Future[User] = ???


}
