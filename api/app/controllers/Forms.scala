package forms

import play.api.data._
import play.api.data.Forms._
import domain.user.{UserCreate, UserCredential}
import domain.business.{BusinessCreate}
import domain.product._

object UserForm {
  val register: Form[UserCreate] = Form(mapping(
    "email" -> nonEmptyText(maxLength=50),
    "name" -> nonEmptyText(maxLength=255),
    "password" -> nonEmptyText(maxLength=50),
    "kind" -> nonEmptyText(maxLength=15),
  )(UserCreate.apply)(UserCreate.unapply))

  val login: Form[UserCredential] = Form(mapping(
    "email" -> nonEmptyText(maxLength=50),
    "password" -> nonEmptyText(maxLength=50)
  )(UserCredential.apply)(UserCredential.unapply))
}

object BusinessForm {
  val createBusiness: Form[BusinessCreate] = Form(mapping(
    "owner" -> uuid,
    "name" -> nonEmptyText(maxLength=255),
  )(BusinessCreate.apply)(BusinessCreate.unapply))
}

object ProductForm {
  val createProduct: Form[ProductCreate] = Form(mapping(
    "idBusiness" -> uuid,
    "name" -> nonEmptyText(maxLength=255),
    "description" -> nonEmptyText,
    "price" -> bigDecimal,
    "stock" -> longNumber
  )(ProductCreate.apply)(ProductCreate.unapply))
}
