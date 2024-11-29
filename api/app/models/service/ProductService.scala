package service.product

import javax.inject._
import play.api._
import play.api.mvc._
import java.util.UUID

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global

import cats.data._
import cats.implicits._

import repo.product._
import domain.product._
@Singleton
class ProductService @Inject()
(
  val productRepo: ProductRepo
) {
  
  def add(product: ProductCreate, id: UUID): EitherT[Future, String, Product] = for {
    _ <- OptionT(productRepo.getById(product.idBusiness)).toLeft(())
        .leftMap(_ => "Product name already exists.")
    result <- EitherT.liftF(productRepo.add(product.toDomain(), id))
  } yield result

}