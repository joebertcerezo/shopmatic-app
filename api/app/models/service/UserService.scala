package service.user

import domain.user._
import repo.user._
import java.util.UUID
import javax.inject._
import play.api._
import play.api.mvc._
import scala.concurrent.{ Future, ExecutionContext }
import cats.data.{ OptionT, EitherT }
import cats.implicits._

@Singleton
class UserService @Inject()
(userRepo: UserRepo)
(using ExecutionContext) {
  /*
    If username exists, don't add.
    Otherwise, proceed adding the user.
  */
  def add(user: UserCreate): EitherT[Future, String, String] = for {
      _ <- OptionT(userRepo.show(user.email)).toLeft(()).leftMap(_ => "Email already registered.")
      result <- EitherT.liftF(userRepo.add(user.toDomain())).map(_ => "Registered successfully.")
    } yield result

  /*
    If credentials match, authenticate user.
    Otherwise, send an unauthorized message.
  */
  def auth(user: UserCredential): EitherT[Future, String, (String,String)] = for {
      u <- OptionT(userRepo.find(user.email, user.password)).toRight("Invalid Credentials.")
      result <- EitherT.liftF(Future((u.email, u.name)))
    } yield result
}
