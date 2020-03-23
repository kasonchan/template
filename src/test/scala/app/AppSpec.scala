package app

import akka.actor.typed.scaladsl.AskPattern._
import akka.actor.typed.{ActorRef, ActorSystem}
import app.App.{guardian, system, init}
import app.Profile.serviceName
import behaviors.{Guardian, System}
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import protocol.message.{Message, Request, Response}
import protocol.status.Starting

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}

/**
  * @author kasonchan
  * @since 2020-03-21
  */
class AppSpec extends AnyWordSpec with Matchers {
  "App" must {
    s"starts correctly where actor system and guardian start correctly" in {
      val expectedSystem: ActorSystem[Message] =
        ActorSystem(System(), serviceName)
      system mustEqual expectedSystem

      val expectedGuardian: ActorRef[Message] =
        expectedSystem.systemActorOf(Guardian(), "guardian")

      import akka.util.Timeout
      implicit val timeout: Timeout = 30.seconds
      implicit val ec: ExecutionContextExecutor = system.executionContext

      val expectedResult = Response(Starting)
      val expectedGuardianResult = Future(Success(Response(Starting)))

      init.map(value => value mustBe expectedResult)

      val expectResult: Future[Message] =
        guardian.ask(ref => Request(protocol.command.Activate, ref))
      expectResult.map(value => value mustBe expectedResult)

      val expectGuardianResult: Future[Message] =
        expectedGuardian.ask(ref => Request(protocol.command.Activate, ref))
      expectGuardianResult.map(value => value mustBe expectedGuardianResult)
    }
  }
}
