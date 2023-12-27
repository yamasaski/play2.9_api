package controllers

import scala.concurrent.Future
import org.scalatest.*
import org.scalatestplus.play.*
import play.api.mvc.*
import play.api.test.*
import play.api.test.Helpers.*
import org.scalatestplus.play.guice.*
import akka.stream.Materializer
import org.mockito.Mockito
import play.api.libs.json.*
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.*
import org.mockito.Mockito.*
import services.DBService
import controllers.forms.SiteForm.SiteForm
import controllers.responses.*

import tag.*

class DBControllerSpec extends PlaySpec with Results with GuiceOneAppPerSuite with MockitoSugar  {

  implicit lazy val materializer: Materializer = app.materializer

  "getSite" should {
    "select成功"in {
      // mock
      val mockDBService = mock[DBService]

      val mockRes = List(GetSiteResponse(111, "テストサイト1", "2023/1/1"), GetSiteResponse(222, "テストサイト2", "2023/1/1"))
      when(mockDBService.getSite).thenReturn(mockRes)

      val controller = new DBController(Helpers.stubControllerComponents(), mockDBService)

      val result: Future[Result] = controller.getSite.apply(FakeRequest())
      val bodyJson = contentAsJson(result)
      bodyJson mustBe Json.toJson(mockRes)
    }
  }

  "insertSite" should {
    "insert成功" in {
      // リクエスト作成
      val json = Json.parse("""{"siteId": "333" ,"siteName": "test"}""")
      val fakeRequest = FakeRequest().withJsonBody(json)

      // mock
      val mockDBService = mock[DBService]

      val res = InsertSiteResponse(333, "test", 1)
      val mockRes = Right(res)
      //　引数
      val form = SiteForm(333, "test")
      when(mockDBService.insertSite(form)).thenReturn(mockRes)

      val controller = new DBController(Helpers.stubControllerComponents(), mockDBService)
      val result: Future[Result] = controller.insertSite.apply(fakeRequest)
      val bodyJson = contentAsJson(result)
      bodyJson mustBe Json.toJson(res)
    }

    "insertしない（サイトIDが存在する）" in {
      // リクエスト作成
      val json = Json.parse("""{"siteId": "333" ,"siteName": "test"}""")
      val fakeRequest = FakeRequest().withJsonBody(json)

      // mock
      val mockDBService = mock[DBService]

      val res = ErrorMessageResponse(Seq("存在するサイトIDです！"))
      val mockRes = Left(res)
      //　引数
      val form = SiteForm(333, "test")
      when(mockDBService.insertSite(form)).thenReturn(mockRes)

      val controller = new DBController(Helpers.stubControllerComponents(), mockDBService)
      val result: Future[Result] = controller.insertSite.apply(fakeRequest)
      val bodyJson = contentAsJson(result)
      bodyJson mustBe Json.toJson(res)
    }
  }
}