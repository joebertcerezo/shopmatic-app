package service.product

import javax.inject._
import play.api._
import play.api.mvc._

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global

import repo.product._
@Singleton
class ProductService @Inject()
(
  val productRepo: ProductRepo
) {
  
}