package controllers

import akka.stream.Materializer
import org.mockito.Mockito.*
import org.scalatest.*
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.*
import org.scalatestplus.play.guice.*
import play.api.libs.json.*
import play.api.mvc.*
import play.api.test.*
import play.api.test.Helpers.*

import scala.concurrent.Future

import tag.*

class HomeControllerSpec extends PlaySpec with Results with GuiceOneAppPerSuite with MockitoSugar {

  "index" should{
    "ok（テキスト）を返す" taggedAs(Tag("Test"), Tag("Test2")) in {
      val controller = new HomeController(Helpers.stubControllerComponents())
      val result = controller.index.apply(FakeRequest())
      val bodyText = contentAsString(result)
      bodyText mustBe "ok"
    }
  }
}