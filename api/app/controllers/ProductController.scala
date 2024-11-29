
package controllers

import service.product._
import javax.inject._
import play.api.mvc._
import play.api.libs.json._
import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class ProductController @Inject()
(
  val controllerComponents: ControllerComponents,
  productService: ProductService
) extends BaseController {
  
}
