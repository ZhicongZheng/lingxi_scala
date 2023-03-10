package infra.db.repository.impl

import common.{Page, PageQuery}
import infra.db.po.ActionPo
import infra.db.po.ActionPo.ActionTable
import infra.db.repository.ActionQueryRepository
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig}
import slick.basic.DatabaseConfig
import slick.jdbc.PostgresProfile
import slick.lifted.TableQuery

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ActionQueryRepositoryImpl @Inject() (private val dbConfigProvider: DatabaseConfigProvider)(implicit
  ec: ExecutionContext
) extends ActionQueryRepository
    with HasDatabaseConfig[PostgresProfile] {

  override protected val dbConfig: DatabaseConfig[PostgresProfile] = dbConfigProvider.get[PostgresProfile]

  private val actions = TableQuery[ActionTable]

  override def get(id: Long): Future[Option[ActionPo]] = ???

  override def list(): Future[Seq[ActionPo]] = ???

  override def count(): Future[Int] = ???

  override def listByPage(pageQuery: PageQuery): Future[Page[ActionPo]] = ???
}
