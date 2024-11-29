package controllers

import service.user._
import util._
import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.i18n.I18nSupport
import scala.concurrent.{ Future, ExecutionContext }
import forms._

@Singleton
class UserController @Inject()
(
  val controllerComponents: ControllerComponents,
  userService: UserService,
) (using ExecutionContext) extends BaseController with I18nSupport {

  def register(sso: Boolean) = Action.async { implicit request =>
    UserForm.register.bindFromRequest().fold (
      error => Future.successful(BadRequest(error.errorsAsJson)),
      data => userService.add(data).value.map {
        case Left(msg) => BadRequest(toMessage(msg))
        case Right(msg) => Ok(toMessage(msg))
      }.recover{ _ => InternalServerError(toMessage("Server Error.")) }
    )
  }

  def login(sso: Boolean) = Action.async { implicit request =>
    UserForm.login.bindFromRequest().fold (
      error => Future.successful(BadRequest(error.errorsAsJson)),
      data => userService.auth(data).value.map {
        case Left(msg) => Unauthorized(toMessage(msg))
        case Right((email, name)) => Ok(Json.obj("email" -> email, "name" -> name))
          .withSession("email" -> data.email)
      }
    ).recover{ _ => InternalServerError(toMessage("Server Error.")) }
  }

  def logout() = Action { implicit request: Request[AnyContent] =>
    Ok(toMessage("Logged out successfully.")).withNewSession
  }

}
