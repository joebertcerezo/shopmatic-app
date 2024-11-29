package forms

import play.api.data._
import play.api.data.Forms._

import domain.product._

object UserForms {
  import domain.user.{UserCreate, UserCredential}

  val registrationForm: Form[UserCreate] = Form(mapping(
    "email" -> nonEmptyText(maxLength=50),
    "name" -> nonEmptyText(maxLength=255),
    "password" -> nonEmptyText(maxLength=50),
    "kind" -> nonEmptyText(maxLength=15),
  )(UserCreate.apply)(UserCreate.unapply))

  val loginForm: Form[UserCredential] = Form(mapping(
    "email" -> nonEmptyText(maxLength=50),
    "password" -> nonEmptyText(maxLength=50)
  )(UserCredential.apply)(UserCredential.unapply))
}

object BusinessForm {
  import domain.business.{BusinessCreate}

  val businessCreate: Form[BusinessCreate] = Form(mapping(
    "owner" -> uuid,
    "name" -> nonEmptyText(maxLength=255),
  )(BusinessCreate.apply)(BusinessCreate.unapply))
}

object ProductForm {
  val productForm: Form[ProductCreate] = Form(mapping(
    "idBusiness" -> uuid,
    "name" -> nonEmptyText(maxLength=255),
    "description" -> nonEmptyText,
    "price" -> bigDecimal,
    "stock" -> longNumber,
    "isHidden" -> boolean
  )(ProductCreate.apply)(ProductCreate.unapply))
}