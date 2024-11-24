package controllers

import service.user._
import util._
import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.i18n.I18nSupport
import scala.concurrent.{ Future, ExecutionContext }
import cats.data.EitherT
import cats.implicits._

@Singleton
class UserController @Inject()
(
  val controllerComponents: ControllerComponents,
  userService: UserService
) (using ExecutionContext) extends BaseController with I18nSupport {

  import forms.UserForms._

  def register() = Action.async { implicit request =>
    registrationForm.bindFromRequest().fold (
      error => Future.successful(BadRequest(error.errorsAsJson)),
      data => userService.add(data).map {
        case true => Ok(toMessage("Registered successfully."))
        case _ => BadRequest(toMessage("Email already registered."))
      }
    ).recover{ _ => InternalServerError(toMessage("Server Error.")) }
  }

  def login() = Action.async { implicit request =>
    loginForm.bindFromRequest().fold(
      error => Future.successful(BadRequest(error.errorsAsJson)),
      data => userService.auth(data).map {
        case Some(name) => Ok(Json.obj("name" -> name))
                            .withSession("email" -> data.email)
        case None => Unauthorized(toMessage("Invalid credentials."))
      }
    ).recover{ _ => InternalServerError(toMessage("Server Error.")) }
  }

  def logout() = Action { implicit request: Request[AnyContent] => Ok.withNewSession }

}
