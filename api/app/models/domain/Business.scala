package domain.business

import java.util.UUID
import play.api.libs.json._

case class Business (
  owner: String,
  name: String,
  id: UUID = UUID.randomUUID()
)

object Business {
  given Writes[Business] = Json.writes[Business]
}

case class BusinessCreate (
  owner: String,
  name: String
) {
  def toDomain() = Business(owner, name)
}

object BusinessCreate {
  def unapply(b: BusinessCreate):
    Option[(String, String)] =
      Some((b.owner, b.name))
}
