package domain.user

import play.api.libs.json._
import java.util.UUID

case class User (
  email: String,
  name: String,
  password: String,
  kind: String,
  id: UUID = UUID.randomUUID()
)

case class UserCreate (
  email: String,
  name: String,
  password: String,
  kind: String
) {
  def toDomain() = User(email, name, password, kind)
}

object UserCreate {
  def unapply(u: UserCreate):
    Option[(String, String, String, String)] =
      Some((u.email, u.name, u.password, u.kind))
}

case class UserCredential (
  email: String,
  password: String
)

object UserCredential {
  def unapply(u: UserCredential):
    Option[(String, String)] =
      Some((u.email, u.password))
}
