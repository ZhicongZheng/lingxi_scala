package infra.db.repository.impl

import common.Constant
import domain.action.{Action, ActionRepository}
import infra.db.assembler.ActionAssembler._
import infra.db.po.ActionPo.ActionTable
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig}
import slick.basic.DatabaseConfig
import slick.jdbc.PostgresProfile
import slick.jdbc.PostgresProfile.api._
import slick.lifted.TableQuery

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ActionRepositoryImpl @Inject() (private val dbConfigProvider: DatabaseConfigProvider)(implicit
  ec: ExecutionContext
) extends ActionRepository
    with HasDatabaseConfig[PostgresProfile] {

  override protected val dbConfig: DatabaseConfig[PostgresProfile] = dbConfigProvider.get[PostgresProfile]

  private val actions = TableQuery[ActionTable]

  override def save(action: Action): Future[Long] =
    action.id match {
      case Constant.domainCreateId => db.run(actions returning actions.map(_.id) += action)
      case _                       => db.run(actions.filter(_.id === action.id).update(action).map(_.toLong))
    }

  override def get(id: Long): Future[Option[Action]] =
    db.run(actions.filter(_.id === id).result.headOption).map(opt => opt.map(toDo))

  override def remove(id: Long): Future[Unit] = db.run(actions.filter(_.id === id).delete).map(_ => ())

}
