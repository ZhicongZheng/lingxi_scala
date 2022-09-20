package auth.repository.impl

import auth.domain.{User, UserRepository}
import auth.repository.dao.UserDao

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserRepositoryImpl @Inject()(userDao: UserDao)
                                  (implicit ec: ExecutionContext) extends UserRepository{

  override def findById(id: Long): Future[User] = ???

  override def list(): Future[Seq[User]] = {
    userDao.list().map(users => users.map(u => u.toDo))
  }

  override def findByUsername(username: String): Future[Option[User]] = {
    userDao.findByUsername(username).map(userPoOpt => userPoOpt.map(po => po.toDo))
  }

  override def create(user: User): Future[User] = ???

  override def update(user: User): Future[User] = ???

  override def delete(id: Long): Future[User] = ???


}
