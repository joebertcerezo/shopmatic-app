package domain.user

import play.api.libs.json._

case class User (
  email: String,
  name: String,
  password: String,
  kind: String
)

object User {
  def unapply(u: User):
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
