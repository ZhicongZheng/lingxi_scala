import auth.domain.UserRepository
import auth.repository.impl.UserRepositoryImpl
import com.google.inject.{AbstractModule, Singleton}
import common.oss.{AliyunOssRepository, OssRepository}

class Module extends AbstractModule {

  override def configure(): Unit = {
    bind(classOf[UserRepository]).to(classOf[UserRepositoryImpl]).in(classOf[Singleton])
    bind(classOf[OssRepository]).to(classOf[AliyunOssRepository]).in(classOf[Singleton])
  }

}
