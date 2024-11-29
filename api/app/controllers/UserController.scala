package controllers

import service.user._
import util._
import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.i18n.I18nSupport
import scala.concurrent.{ Future, ExecutionContext }

@Singleton
class UserController @Inject()
(
  val controllerComponents: ControllerComponents,
  userService: UserService,
  val secureAction: SecureAction
) (using ExecutionContext) extends BaseController with I18nSupport {

  import forms.UserForms._

  def register() = Action.async { implicit request =>
    registrationForm.bindFromRequest().fold (
      error => Future.successful(BadRequest(error.errorsAsJson)),
      data => userService.add(data).value.map {
        case Left(msg) => BadRequest(toMessage(msg))
        case Right(msg) => Ok(toMessage(msg))
      }.recover{ _ => InternalServerError(toMessage("Server Error.")) }
    )
  }

  def login() = Action.async { implicit request =>
    loginForm.bindFromRequest().fold (
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
