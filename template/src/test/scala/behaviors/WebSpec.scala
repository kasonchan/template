package behaviors

import akka.actor.testkit.typed.scaladsl.ActorTestKit
import app.Profile
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import protocol.command.{ Activate, Status }
import protocol.message.{ Message, Request, Response }
import protocol.status.{ Fatal, Ready, Starting }

/**
 * @author kasonchan
 * @since 2020-03-28
 */
class WebSpec extends AnyWordSpec with Matchers {
  private val timeoutInMilliseconds = Profile.bindingTimeoutInMilliseconds

  "Starting Web" must {
    "returns Ready after receiving Activate request if server bind successfully" in {
      val testKit = ActorTestKit()
      val web = testKit.spawn(Web(), "web")
      val probe = testKit.createTestProbe[Message]()
      val expectedResponse = Response(Ready)

      web ! Request(Activate, probe.ref)
      probe.expectMessage(timeoutInMilliseconds, expectedResponse)
      testKit.shutdownTestKit
    }

    "returns Fatal after receiving Activate request if server bind failed" in {
      val failedPort = 9000
      val testKit = ActorTestKit()
      val occupiedWeb =
        testKit.spawn(Web(port = failedPort), "occupiedWeb")
      val web = testKit.spawn(Web(port = failedPort), "web")
      val probe = testKit.createTestProbe[Message]()
      val expectedResponse1 = Response(Ready)
      val expectedResponse2 = Response(Fatal)

      occupiedWeb ! Request(Activate, probe.ref)
      probe.expectMessage(timeoutInMilliseconds, expectedResponse1)
      web ! Request(Activate, probe.ref)
      probe.expectMessage(timeoutInMilliseconds, expectedResponse2)
      testKit.shutdownTestKit
    }

    "returns Starting after receiving Status request" in {
      val testKit = ActorTestKit()
      val web = testKit.spawn(Web(), "web")
      val probe = testKit.createTestProbe[Message]()
      val expectedResponse = Response(Starting)

      web ! Request(Status, probe.ref)
      probe.expectMessage(timeoutInMilliseconds, expectedResponse)
      testKit.shutdownTestKit
    }

    "returns nothing after receiving Response request" in {
      val testKit = ActorTestKit()
      val web = testKit.spawn(Web(), "web")
      val probe = testKit.createTestProbe[Message]()

      web ! Response(Starting)
      probe.expectNoMessage(timeoutInMilliseconds)
      testKit.shutdownTestKit
    }
  }

  "Ready Web" must {
    "returns Ready after receiving Status request" in {
      val testKit = ActorTestKit()
      val web = testKit.spawn(Web(), "web")
      val probe = testKit.createTestProbe[Message]()
      val expectedResponse = Response(Ready)

      web ! Request(Activate, probe.ref)
      probe.expectMessage(timeoutInMilliseconds, expectedResponse)
      web ! Request(Status, probe.ref)
      probe.expectMessage(timeoutInMilliseconds, expectedResponse)
      testKit.shutdownTestKit
    }

    "returns nothing after receiving Activate request" in {
      val testKit = ActorTestKit()
      val web = testKit.spawn(Web(), "web")
      val probe = testKit.createTestProbe[Message]()
      val expectedResponse = Response(Ready)

      web ! Request(Activate, probe.ref)
      probe.expectMessage(timeoutInMilliseconds, expectedResponse)
      web ! Request(Activate, probe.ref)
      probe.expectNoMessage(timeoutInMilliseconds)
      testKit.shutdownTestKit
    }
  }

  "Fatal Web" must {
    "returns Fatal after receiving Status request" in {
      val failedPort = 9000
      val testKit = ActorTestKit()
      val occupiedWeb =
        testKit.spawn(Web(port = failedPort), "occupiedWeb")
      val web = testKit.spawn(Web(port = failedPort), "web")
      val probe = testKit.createTestProbe[Message]()
      val expectedResponse1 = Response(Ready)
      val expectedResponse2 = Response(Fatal)

      occupiedWeb ! Request(Activate, probe.ref)
      probe.expectMessage(timeoutInMilliseconds, expectedResponse1)
      web ! Request(Activate, probe.ref)
      probe.expectMessage(timeoutInMilliseconds, expectedResponse2)
      web ! Request(Status, probe.ref)
      probe.expectMessage(timeoutInMilliseconds, expectedResponse2)
      testKit.shutdownTestKit
    }

    "returns nothing after receiving Activate request" in {
      val failedPort = 9000
      val testKit = ActorTestKit()
      val occupiedWeb =
        testKit.spawn(Web(port = failedPort), "occupiedWeb")
      val web = testKit.spawn(Web(port = failedPort), "web")
      val probe = testKit.createTestProbe[Message]()
      val expectedResponse1 = Response(Ready)
      val expectedResponse2 = Response(Fatal)

      occupiedWeb ! Request(Activate, probe.ref)
      probe.expectMessage(timeoutInMilliseconds, expectedResponse1)
      web ! Request(Activate, probe.ref)
      probe.expectMessage(timeoutInMilliseconds, expectedResponse2)
      web ! Request(Activate, probe.ref)
      probe.expectNoMessage(timeoutInMilliseconds)
      testKit.shutdownTestKit
    }
  }
}
