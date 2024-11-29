
package controllers

import service.product._
import javax.inject._
import play.api.mvc._
import play.api.libs.json._
import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global

import domain.product._

@Singleton
class ProductController @Inject()
(
  val controllerComponents: ControllerComponents,
  productService: ProductService
) extends BaseController {
  

  def add(id: String) = Action.async(parse.json) { implicit request =>
    request.body.validate[ProductCreate].fold(
      error => Future.successful(BadRequest(JsError.toJson(error))),
      data => productService.add(data, java.util.UUID.fromString(id)).value.map {
        case Left(msg) => BadRequest(msg)
        case Right(msg) => Ok(Json.toJson(msg))
      }.recover{ _ => InternalServerError("Server Error.") }
    )
  }
}
