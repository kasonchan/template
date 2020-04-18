package protocol.message

import akka.actor.typed.ActorRef
import protocol.command.Command
import protocol.status.Status

/**
 * @author kasonchan
 * @since 2020-03-20
 */
sealed trait Message
case class Request(command: Command, replyTo: ActorRef[Message]) extends Message
case class Response(status: Status) extends Message
