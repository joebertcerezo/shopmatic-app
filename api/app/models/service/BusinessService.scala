package service.business

import domain.business._
import repo.business._
import java.util.UUID
import javax.inject._
import play.api._
import play.api.mvc._
import scala.concurrent.{ Future, ExecutionContext }
import cats.data.{ OptionT, EitherT }
import cats.implicits._

@Singleton
class UserService @Inject()
(businessRepo: BusinessRepo)
(using ExecutionContext) {
  /*
    If business name exists already for the owner, don't add.
    Otherwise, proceed adding the business.
  */
  def add(business: BusinessCreate): EitherT[Future, String, Business] = for {
      _ <- OptionT(businessRepo.find(business.idOwner, business.name)).toLeft(())
          .leftMap(_ => "Business name already taken.")
      result <- EitherT.liftF(businessRepo.add(business.toDomain()))
    } yield result
}
