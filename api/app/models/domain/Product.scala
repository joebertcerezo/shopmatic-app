
package domain.product

import play.api.libs.json._
import java.util.UUID

case class Product(
  idBusiness: UUID,
  name: String,
  description: String,
  price: BigDecimal,
  stock: Long,
  id: UUID = UUID.randomUUID(),
  isHidden: Boolean = false
)

object Product {
  given Writes[Product] = Json.writes[Product]
}

case class ProductCreate(
  idBusiness: UUID,
  name: String,
  description: String,
  price: BigDecimal,
  stock: Long,
) {
  def toDomain() = Product(idBusiness, name, description, price, stock)
}

object ProductCreate {
  def unapply(p: ProductCreate):
    Option[(UUID, String, String, BigDecimal, Long)] =
      Some((p.idBusiness, p.name, p.description, p.price, p.stock))
}
