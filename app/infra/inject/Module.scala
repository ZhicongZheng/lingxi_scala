package infra.inject

import com.google.inject.{AbstractModule, Singleton}
import domain.auth.repository.{PermissionRepository, RoleRepository}
import domain.user.repository.UserRepository
import infra.db.repository.{PermissionRepositoryImpl, RoleRepositoryImpl, UserRepositoryImpl}
import infra.oss.{AliyunOssRepository, OssRepository}

class Module extends AbstractModule {

  override def configure(): Unit = {
    bind(classOf[UserRepository]).to(classOf[UserRepositoryImpl]).in(classOf[Singleton])
    bind(classOf[RoleRepository]).to(classOf[RoleRepositoryImpl]).in(classOf[Singleton])
    bind(classOf[PermissionRepository]).to(classOf[PermissionRepositoryImpl]).in(classOf[Singleton])
    bind(classOf[OssRepository]).to(classOf[AliyunOssRepository]).in(classOf[Singleton])
  }

}
