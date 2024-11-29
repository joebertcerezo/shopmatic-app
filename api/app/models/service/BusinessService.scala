package service.business

import domain.business._

import repo.business._
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
(userRepo: UserRepo, businessRepo: BusinessRepo)
(using ExecutionContext) {
  /*
    If business name exists already for the owner, don't add.
    Otherwise, proceed adding the business.
  */
  def add(business: BusinessCreate): EitherT[Future, String, Business] = for {
      _ <- OptionT(businessRepo.find(business.owner, business.name)).toLeft(())
          .leftMap(_ => "Business name already exists.")
      result <- EitherT.liftF(businessRepo.add(business.toDomain()))
    } yield result

  /*
    If user exists, get all businesses of that user.
    Otherwise, return a bad request message.
  */
  def get(email: String): EitherT[Future, String, Seq[Business]] = for {
      _ <- OptionT(userRepo.show(email)).toRight("User doesn't exist.")
      result <- EitherT.liftF(businessRepo.get(email))
    } yield result

}
