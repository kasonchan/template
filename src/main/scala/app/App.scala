package app

import akka.actor.typed.scaladsl.AskPattern._
import akka.actor.typed.{ActorRef, ActorSystem}
import akka.util.Timeout
import app.Profile.{bindingTimeoutInMilliseconds, serviceName}
import behaviors.{Guardian, System}
import protocol.message.{Message, Request}

import scala.concurrent.{ExecutionContextExecutor, Future}

/**
  * @author kasonchan
  * @since 2020-03-15
  */
object App {
  implicit val system: ActorSystem[Message] = ActorSystem(System(), serviceName)
  implicit val guardian: ActorRef[Message] =
    system.systemActorOf(Guardian(), "guardian")
  implicit val timeout: Timeout = bindingTimeoutInMilliseconds
  implicit val ec: ExecutionContextExecutor = system.executionContext
  lazy val init: Future[Message] =
    guardian.ask(ref => Request(protocol.command.Activate, ref))

  def main(args: Array[String]): Unit = {
    init
  }
}
