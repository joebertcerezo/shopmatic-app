package repo.user

import domain.user._
import java.util.UUID
import javax.inject._
import slick.jdbc.PostgresProfile
import play.api.db.slick.DatabaseConfigProvider
import scala.concurrent.{ Future, ExecutionContext }

@Singleton
class UserRepo @Inject()
(dbcp: DatabaseConfigProvider)
(using ExecutionContext) {

  val dbConfig = dbcp.get[PostgresProfile]
  import dbConfig._
  import profile.api._

  final class UserTable(t: Tag) extends Table[User](t, "USER") {
    def id = column[UUID]("ID", O.PrimaryKey)
    def email = column[String]("EMAIL", O.Length(50), O.PrimaryKey)
    def name = column[String]("NAME", O.Length(255))
    def password = column[String]("PASSWORD", O.Length(50))
    def kind = column[String]("KIND", O.Length(15))
    def * = (email, name, password, kind, id).mapTo[User]
  }

  lazy val users = TableQuery[UserTable]

  def add(user: User): Future[Int] = db.run { users += user }

  def find(email: String, pass: String): Future[Option[User]] = db.run {
      users
        .filter(u => u.email === email && u.password === pass)
        .result
        .headOption
    }

  def show(email: String): Future[Option[User]] = db.run {
      users.filter(_.email === email).result.headOption
    }
}
