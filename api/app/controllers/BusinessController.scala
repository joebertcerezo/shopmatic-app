package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.i18n.I18nSupport
import scala.concurrent.{ Future, ExecutionContext }

import service.business._
import forms._

@Singleton
class BusinessController @Inject()
(
  val controllerComponents: ControllerComponents, val businessService: BusinessService
) (using ExecutionContext) extends BaseController with I18nSupport {


  def add() = Action.async { implicit request =>
    BusinessForm.businessCreate.bindFromRequest().fold (
      error => Future.successful(BadRequest(error.errorsAsJson)),
      data => businessService.add(data).value.map {
        case Left(msg) => BadRequest(msg)
        case Right(msg) => Ok(Json.obj("message" -> msg))
      }.recover{ _ => InternalServerError("Server Error") }
    )
  }
}
