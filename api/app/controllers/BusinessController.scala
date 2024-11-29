package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.i18n.I18nSupport
import scala.concurrent.{ Future, ExecutionContext }

@Singleton
class BusinessController @Inject()
(
  val controllerComponents: ControllerComponents,
) (using ExecutionContext) extends BaseController with I18nSupport {

  import forms.UserForms._

  def get() = TODO

}
