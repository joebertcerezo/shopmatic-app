package repo.business

import domain.business._
import java.util.UUID
import javax.inject._
import slick.jdbc.PostgresProfile
import play.api.db.slick.DatabaseConfigProvider
import scala.concurrent.{ Future, ExecutionContext }

@Singleton
class BusinessRepo @Inject()
(dbcp: DatabaseConfigProvider)
(using ExecutionContext) {

  val dbConfig = dbcp.get[PostgresProfile]
  import dbConfig._
  import profile.api._

  final class BusinessTable(t: Tag) extends Table[Business](t, "BUSINESS") {
    def id = column[UUID]("ID", O.PrimaryKey)
    def owner = column[String]("OWNER")
    def name = column[String]("NAME", O.Length(255))
    def * = (owner, name, id).mapTo[Business]
  }

  lazy val businesses = TableQuery[BusinessTable]

  def add(business: Business): Future[Business] = db.run {
      businesses returning businesses += business
    }

  def get(email: String): Future[Seq[Business]] = db.run {
      businesses.filter(_.owner === email).result
    }

  def find(email: String, name: String): Future[Option[Business]] = db.run {
      businesses
        .filter(b => b.owner === email && b.name === name)
        .result
        .headOption
    }

  def update(id: UUID, newName: String): Future[Int] = db.run {
      businesses.filter(_.id === id).map(_.name).update(newName)
    }
}

