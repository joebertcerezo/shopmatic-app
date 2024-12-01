package repo.business

import domain.business._
import repo.user._
import java.util.UUID
import javax.inject._
import slick.jdbc.PostgresProfile
import play.api.db.slick.DatabaseConfigProvider
import scala.concurrent.{ Future, ExecutionContext }

@Singleton
class BusinessRepo @Inject()
(dbcp: DatabaseConfigProvider, val userRepo: UserRepo)
(using ExecutionContext) {

  val dbConfig = dbcp.get[PostgresProfile]
  import dbConfig._
  import profile.api._

  final class BusinessTable(t: Tag) extends Table[Business](t, "BUSINESS") {
    def id = column[UUID]("ID", O.PrimaryKey)
    def owner = column[UUID]("OWNER")
    def name = column[String]("NAME", O.Length(255))
    def ownewrFK = foreignKey("OWNER_FK", owner, userRepo.users)(_.id, onDelete = ForeignKeyAction.Cascade)
    def * = (owner, name, id).mapTo[Business]
  }

  lazy val businesses = TableQuery[BusinessTable]

  def add(business: Business): Future[Business] = db.run {
      businesses returning businesses += business
    }

  def get(owner: UUID): Future[Seq[Business]] = db.run {
      businesses.filter(_.owner === owner).result
    }

  def find(owner: UUID, name: String): Future[Option[Business]] = db.run {
      businesses
        .filter(b => b.owner === owner && b.name === name)
        .result
        .headOption
    }

  def update(id: UUID, newName: String): Future[Int] = db.run {
      businesses.filter(_.id === id).map(_.name).update(newName)
    }
}

