package behaviors

import akka.actor.testkit.typed.CapturedLogEvent
import akka.actor.testkit.typed.scaladsl.{ BehaviorTestKit, TestInbox }
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.slf4j.event.Level
import protocol.command.{ Activate, Status }
import protocol.message.{ Message, Request, Response }
import protocol.status.{ Fatal, Ready, Starting }

/**
 * @author kasonchan
 * @since 2020-03-21
 */
class GuardianSpec extends AnyWordSpec with Matchers {
  "Starting Guardian" must {
    "returns Starting after receiving Activate request" in {
      val behaviorTestKit = BehaviorTestKit(Guardian())
      val guardian = TestInbox[Message]()
      val expectedMessage = Response(Starting)
      val expectedLogEntries = Seq(
        CapturedLogEvent(Level.INFO, "[STARTING]"),
        CapturedLogEvent(Level.INFO, "[STARTING]-Activate->[READY]"))

      behaviorTestKit.run(Request(Activate, guardian.ref))
      guardian.expectMessage(expectedMessage)
      behaviorTestKit.logEntries() mustBe expectedLogEntries
    }

    "returns Starting after receiving Status request" in {
      val behaviorTestKit = BehaviorTestKit(Guardian())
      val guardian = TestInbox[Message]()
      val expectedMessage = Response(Starting)

      behaviorTestKit.run(Request(Status, guardian.ref))
      guardian.expectMessage(expectedMessage)
      behaviorTestKit.logEntries() mustBe Seq()
    }

    "returns Starting after receiving Activate request and Ready response" in {
      val behaviorTestKit = BehaviorTestKit(Guardian())
      val guardian = TestInbox[Message]()
      val expectedMessage1 = Response(Starting)
      val expectedLogEntries1 = Seq(
        CapturedLogEvent(Level.INFO, "[STARTING]"),
        CapturedLogEvent(Level.INFO, "[STARTING]-Activate->[READY]"))
      val expectedLogEntries2 = Seq(
        CapturedLogEvent(Level.INFO, "[STARTING]"),
        CapturedLogEvent(Level.INFO, "[STARTING]-Activate->[READY]"),
        CapturedLogEvent(Level.INFO, "[READY]"))

      behaviorTestKit.run(Request(Activate, guardian.ref))
      guardian.expectMessage(expectedMessage1)
      behaviorTestKit.logEntries() mustBe expectedLogEntries1
      behaviorTestKit.run(Response(Ready))
      behaviorTestKit.logEntries() mustBe expectedLogEntries2
    }

    "returns Starting after receiving Activate request and Fatal response" in {
      val behaviorTestKit = BehaviorTestKit(Guardian())
      val guardian = TestInbox[Message]()
      val expectedMessage1 = Response(Starting)
      val expectedLogEntries1 = Seq(
        CapturedLogEvent(Level.INFO, "[STARTING]"),
        CapturedLogEvent(Level.INFO, "[STARTING]-Activate->[READY]"))
      val expectedLogEntries2 = Seq(
        CapturedLogEvent(Level.INFO, "[STARTING]"),
        CapturedLogEvent(Level.INFO, "[STARTING]-Activate->[READY]"),
        CapturedLogEvent(Level.INFO, "[FATAL]"))

      behaviorTestKit.run(Request(Activate, guardian.ref))
      guardian.expectMessage(expectedMessage1)
      behaviorTestKit.logEntries() mustBe expectedLogEntries1
      behaviorTestKit.run(Response(Fatal))
      behaviorTestKit.logEntries() mustBe expectedLogEntries2
    }

    "returns nothing after receiving Response request" in {
      val behaviorTestKit = BehaviorTestKit(Guardian())
      val guardian = TestInbox[Message]()
      val expectedLogEntries = Seq(CapturedLogEvent(Level.INFO, "[STARTING]"))

      behaviorTestKit.run(Response(Starting))
      guardian.receiveAll() mustBe Seq()
      behaviorTestKit.logEntries() mustBe expectedLogEntries
    }

    "returns Ready after receiving Activate request, Ready response and Status request" in {
      val behaviorTestKit = BehaviorTestKit(Guardian())
      val guardian = TestInbox[Message]()
      val expectedMessage1 = Response(Starting)
      val expectedMessage2 = Response(Ready)
      val expectedLogEntries = Seq(
        CapturedLogEvent(Level.INFO, "[STARTING]"),
        CapturedLogEvent(Level.INFO, "[STARTING]-Activate->[READY]"),
        CapturedLogEvent(Level.INFO, "[READY]"))

      behaviorTestKit.run(Request(Activate, guardian.ref))
      behaviorTestKit.run(Response(Ready))
      behaviorTestKit.run(Request(Status, guardian.ref))
      guardian.receiveAll() mustBe Seq(expectedMessage1, expectedMessage2)
      behaviorTestKit.logEntries() mustBe expectedLogEntries
    }

    "returns Ready after receiving Activate request, Ready response, Activate request and Status request" in {
      val behaviorTestKit = BehaviorTestKit(Guardian())
      val guardian = TestInbox[Message]()
      val expectedMessage1 = Response(Starting)
      val expectedMessage2 = Response(Ready)
      val expectedLogEntries = Seq(
        CapturedLogEvent(Level.INFO, "[STARTING]"),
        CapturedLogEvent(Level.INFO, "[STARTING]-Activate->[READY]"),
        CapturedLogEvent(Level.INFO, "[READY]"))

      behaviorTestKit.run(Request(Activate, guardian.ref))
      behaviorTestKit.run(Response(Ready))
      behaviorTestKit.run(Request(Activate, guardian.ref))
      behaviorTestKit.run(Request(Status, guardian.ref))
      guardian.receiveAll() mustBe Seq(expectedMessage1, expectedMessage2)
      behaviorTestKit.logEntries() mustBe expectedLogEntries
    }

    "returns Fatal after receiving Activate request, Fatal response and Status request" in {
      val behaviorTestKit = BehaviorTestKit(Guardian())
      val guardian = TestInbox[Message]()
      val expectedMessage1 = Response(Starting)
      val expectedMessage2 = Response(Fatal)
      val expectedLogEntries = Seq(
        CapturedLogEvent(Level.INFO, "[STARTING]"),
        CapturedLogEvent(Level.INFO, "[STARTING]-Activate->[READY]"),
        CapturedLogEvent(Level.INFO, "[FATAL]"))

      behaviorTestKit.run(Request(Activate, guardian.ref))
      behaviorTestKit.run(Response(Fatal))
      behaviorTestKit.run(Request(Status, guardian.ref))
      guardian.receiveAll() mustBe Seq(expectedMessage1, expectedMessage2)
      behaviorTestKit.logEntries() mustBe expectedLogEntries
    }

    "returns Fatal after receiving Activate request, Fatal response, Activate and Status request" in {
      val behaviorTestKit = BehaviorTestKit(Guardian())
      val guardian = TestInbox[Message]()
      val expectedMessage1 = Response(Starting)
      val expectedMessage2 = Response(Fatal)
      val expectedLogEntries = Seq(
        CapturedLogEvent(Level.INFO, "[STARTING]"),
        CapturedLogEvent(Level.INFO, "[STARTING]-Activate->[READY]"),
        CapturedLogEvent(Level.INFO, "[FATAL]"))

      behaviorTestKit.run(Request(Activate, guardian.ref))
      behaviorTestKit.run(Response(Fatal))
      behaviorTestKit.run(Request(Activate, guardian.ref))
      behaviorTestKit.run(Request(Status, guardian.ref))
      guardian.receiveAll() mustBe Seq(expectedMessage1, expectedMessage2)
      behaviorTestKit.logEntries() mustBe expectedLogEntries
    }
  }
}
