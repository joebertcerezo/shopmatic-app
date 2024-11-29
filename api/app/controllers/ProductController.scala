
package controllers

import service.product._
import javax.inject._
import play.api.mvc._
import play.api.libs.json._
import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global

import java.util.UUID

import forms.ProductForm._

import domain.product._

@Singleton
class ProductController @Inject()
(
  val controllerComponents: ControllerComponents,
  productService: ProductService
) extends BaseController {
  

  def add(id: UUID) = Action.async(parse.json) { implicit request =>
    productForm.bindFromRequest().fold(
      error => Future.successful(BadRequest(error.errors.map(_.message).mkString(", "))),
      data => productService.add(data, id).value.map {
        case Left(msg) => BadRequest(msg)
        case Right(msg) => Ok(Json.obj("message" -> msg))
      }.recover{ _ => InternalServerError("Server Error.") }
    )
  }
}
