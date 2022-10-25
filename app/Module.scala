import auth.domain.repository.{PermissionRepository, RoleRepository, UserRepository}
import auth.repository.impl.{PermissionRepositoryImpl, RoleRepositoryImpl, UserRepositoryImpl}
import com.google.inject.{AbstractModule, Singleton}
import common.oss.{AliyunOssRepository, OssRepository}

class Module extends AbstractModule {

  override def configure(): Unit = {
    bind(classOf[UserRepository]).to(classOf[UserRepositoryImpl]).in(classOf[Singleton])
    bind(classOf[RoleRepository]).to(classOf[RoleRepositoryImpl]).in(classOf[Singleton])
    bind(classOf[PermissionRepository]).to(classOf[PermissionRepositoryImpl]).in(classOf[Singleton])
    bind(classOf[OssRepository]).to(classOf[AliyunOssRepository]).in(classOf[Singleton])
  }

}
