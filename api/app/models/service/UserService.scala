package service.user

import domain.user._
import repo.user._
import javax.inject._
import play.api._
import play.api.mvc._
import scala.concurrent.{ Future, ExecutionContext }
import cats.data.OptionT
import cats.implicits._

@Singleton
class UserService @Inject()
(userRepo: UserRepo)
(using ExecutionContext) {
  def add(user: UserCreate): Future[Boolean] =
    OptionT(userRepo.show(user.email))
      .map(_ => false)
      .getOrElseF(userRepo.add(user.toDomain()).map(_ => true))

  def auth(user: UserCredential): Future[Option[String]] =
    OptionT(userRepo.find(user.email, user.password))
      .map(user => Some(user.name))
      .getOrElse(None)
}
