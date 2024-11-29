package domain.business

import java.util.UUID
import play.api.libs.json._

case class Business (
  owner: UUID,
  name: String,
  id: UUID = UUID.randomUUID()
)

object Business {
  given Writes[Business] = Json.writes[Business]
  given Reads[Business] = Json.reads[Business]
}

case class BusinessCreate (
  owner: UUID,
  name: String
) {
  def toDomain() = Business(owner, name, UUID.randomUUID())
}

object BusinessCreate {
  def unapply(b: BusinessCreate):
    Option[(UUID, String)] =
      Some((b.owner, b.name))

  given Writes[BusinessCreate] = Json.writes[BusinessCreate]
  given Reads[BusinessCreate] = Json.reads[BusinessCreate]
}
