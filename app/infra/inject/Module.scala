package infra.inject

import com.google.inject.{AbstractModule, Singleton}
import domain.action.ActionRepository
import domain.article.ArticleRepository
import domain.auth.repository.RoleRepository
import domain.user.repository.UserRepository
import infra.db.repository.{ArticleQueryRepository, RoleQueryRepository, UserQueryRepository}
import infra.db.repository.impl.{
  ActionRepositoryImpl,
  ArticleQueryRepositoryImpl,
  ArticleRepositoryImpl,
  RoleQueryRepositoryImpl,
  RoleRepositoryImpl,
  UserQueryRepositoryImpl,
  UserRepositoryImpl
}
import infra.mail.{MailService, MailServiceImpl}
import infra.oss.{AliyunOssRepository, OssRepository}

class Module extends AbstractModule {

  override def configure(): Unit = {
    bind(classOf[OssRepository]).to(classOf[AliyunOssRepository]).in(classOf[Singleton])
    bind(classOf[MailService]).to(classOf[MailServiceImpl]).in(classOf[Singleton])
    bind(classOf[UserRepository]).to(classOf[UserRepositoryImpl]).in(classOf[Singleton])
    bind(classOf[RoleRepository]).to(classOf[RoleRepositoryImpl]).in(classOf[Singleton])
    bind(classOf[UserQueryRepository]).to(classOf[UserQueryRepositoryImpl]).in(classOf[Singleton])
    bind(classOf[RoleQueryRepository]).to(classOf[RoleQueryRepositoryImpl]).in(classOf[Singleton])
    bind(classOf[ArticleRepository]).to(classOf[ArticleRepositoryImpl]).in(classOf[Singleton])
    bind(classOf[ArticleQueryRepository]).to(classOf[ArticleQueryRepositoryImpl]).in(classOf[Singleton])
    bind(classOf[ActionRepository]).to(classOf[ActionRepositoryImpl]).in(classOf[Singleton])
  }

}
