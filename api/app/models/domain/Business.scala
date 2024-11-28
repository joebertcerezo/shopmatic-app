package domain.business

import java.util.UUID
import play.api.libs.json._

case class Business (
  idOwner: UUID,
  name: String,
  id: UUID = UUID.randomUUID()
)

object Business {
  given Writes[Business] = Json.writes[Business]
}

case class BusinessCreate (
  idOwner: UUID,
  name: String
) {
  def toDomain() = Business(idOwner, name)
}

object BusinessCreate {
  def unapply(b: BusinessCreate):
    Option[(UUID, String)] =
      Some((b.idOwner, b.name))
}
