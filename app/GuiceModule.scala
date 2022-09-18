import auth.domain.repository.UserRepository
import auth.domain.repository.impl.UserRepositoryImpl
import com.google.inject.{AbstractModule, Singleton}

class GuiceModule extends AbstractModule {

  override def configure(): Unit = {

    bind(classOf[UserRepository]).to(classOf[UserRepositoryImpl]).in(classOf[Singleton])

  }

}
