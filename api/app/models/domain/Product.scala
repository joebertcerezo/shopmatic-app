
package domain.product

import play.api.libs.json._
import java.util.UUID

case class Product(
  id: UUID,
  idBusiness: UUID,
  name: String,
  description: String,
  price: BigDecimal,
  stock: Long,
  isHidden: Boolean
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
  isHidden: Boolean
) {
  def toDomain() = Product(UUID.randomUUID(), idBusiness, name, description, price, stock, isHidden)
}

object ProductCreate {
  def unapply(p: ProductCreate):
    Option[(UUID, String, String, BigDecimal, Long, Boolean)] =
      Some((p.idBusiness, p.name, p.description, p.price, p.stock, p.isHidden))
}