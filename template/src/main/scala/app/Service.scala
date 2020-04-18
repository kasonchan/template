package app

import akka.actor.typed.scaladsl.AskPattern._
import akka.actor.typed.{ActorRef, ActorSystem}
import akka.util.Timeout
import behaviors.{Guardian, System}
import protocol.message.{Message, Request}

import scala.concurrent.{ExecutionContextExecutor, Future}

/**
  * @author kasonchan
  * @since 2020-04-04
  */
object Service {
  implicit val system: ActorSystem[Message] =
    ActorSystem(System(), Profile.serviceName)
  implicit val guardian: ActorRef[Message] =
    system.systemActorOf(Guardian(), "guardian")
  implicit val timeout: Timeout = Profile.bindingTimeoutInMilliseconds
  implicit val ec: ExecutionContextExecutor = system.executionContext
  val init: Future[Message] =
    guardian.ask(ref => Request(protocol.command.Activate, ref))
  def status: Future[Message] =
    guardian.ask(ref => Request(protocol.command.Status, ref))
}
