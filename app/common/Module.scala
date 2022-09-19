package common

import auth.domain.repository.UserRepository
import auth.domain.repository.dao.UserDao
import auth.domain.repository.impl.UserRepositoryImpl
import com.google.inject.{AbstractModule, Singleton}

class Module extends AbstractModule {

  override def configure(): Unit = {

    bind(classOf[UserRepository]).to(classOf[UserRepositoryImpl]).in(classOf[Singleton])
    bind(classOf[UserDao]).in(classOf[Singleton])

  }

}
