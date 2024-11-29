package forms

import play.api.data._
import play.api.data.Forms._

object UserForms {
  import domain.user.{User, UserCredential}

  val registrationForm: Form[User] = Form(mapping(
    "email" -> nonEmptyText(maxLength=50),
    "name" -> nonEmptyText(maxLength=255),
    "password" -> nonEmptyText(maxLength=50),
    "kind" -> nonEmptyText(maxLength=15),
  )(User.apply)(User.unapply))

  val loginForm: Form[UserCredential] = Form(mapping(
    "email" -> nonEmptyText(maxLength=50),
    "password" -> nonEmptyText(maxLength=50)
  )(UserCredential.apply)(UserCredential.unapply))
}

object BusinessForms {
  import domain.business.{BusinessCreate}

  val businessForm: Form[BusinessCreate] = Form(mapping(
    "owner" -> email,
    "name" -> nonEmptyText(maxLength=255),
  )(BusinessCreate.apply)(BusinessCreate.unapply))
}
