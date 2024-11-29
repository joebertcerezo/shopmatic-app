package repo.product

import domain.product._
import repo.business._
import java.util.UUID
import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{Future, ExecutionContext}

@Singleton
class ProductRepository @Inject()(
  dbConfigProvider: DatabaseConfigProvider, 
  val business: BusinessRepo
)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private class ProductTable(tag: Tag) 
    extends Table[Product](tag, "PRODUCT") {
    def id = column[UUID]("ID", O.PrimaryKey)
    def idBusiness = column[UUID]("ID_BUSINESS")
    def name = column[String]("NAME", O.Length(255))
    def description = column[String]("DESCRIPTION")
    def price = column[BigDecimal]("PRICE")
    def stock = column[Long]("STOCK")
    def isHidden = column[Boolean]("IS_HIDDEN")

    def idBusinessFK = foreignKey(
      "ID_BUSINESS_FK", idBusiness, business.businesses
    )(_.id, onDelete = ForeignKeyAction.Cascade)

    def * = (
      id, idBusiness, name, description, price, stock, isHidden
    ).mapTo[Product]
  }

  private val products = TableQuery[ProductTable]

  def list(): Future[Seq[Product]] = db.run {
    products.result
  }

  def getById(id: UUID): Future[Option[Product]] = db.run {
    products.filter(_.id === id).result.headOption
  }

  def create(product: Product): Future[Product] = db.run {
    (products returning products.map(_.id) into (
      (product, id) => product.copy(id = id)
    )) += product
  }

  def update(id: UUID, product: Product): Future[Int] = db.run {
    products.filter(_.id === id).update(product)
  }

  def delete(id: UUID): Future[Int] = db.run {
    products.filter(_.id === id).delete
  }
}