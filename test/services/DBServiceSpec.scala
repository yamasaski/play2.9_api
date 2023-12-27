package services

import akka.stream.Materializer
import controllers.responses.GetSiteResponse
import models.*
import org.mockito.Mockito.*
import org.scalatest.*
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.*
import org.scalatestplus.play.guice.*
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.*
import play.api.mvc.*
import play.api.test.*
import play.api.test.Helpers.*
import repositories.*
import services.DBService

import java.util.Date
import scala.concurrent.Future
import scala.reflect.ClassTag

import tag.*

class DBServiceSpec extends PlaySpec with Results with GuiceOneAppPerSuite with MockitoSugar with Inject  {

  // 参考
  // https://tanoshiilife.wordpress.com/2016/04/20/play-framework-2-4-scala-specs2-guice-di-%E3%81%A7%E3%83%86%E3%82%B9%E3%83%88%E7%B4%A0%E4%BA%BA%E3%81%8C%E3%83%86%E3%82%B9%E3%83%88%E3%81%AB%E6%8C%91%E6%88%A6%E3%81%97%E3%81%9F%E8%A9%B1/
  lazy val siteMstRepository = inject[SiteMstRepository]

  "DBService" should {
    "getSite" taggedAs NotDB in {
      // mock
      val mockSiteMstRepository = mock[SiteMstRepository]

      val date = new Date()
      val list = List(SiteMst(111,"テストサイト1",date), SiteMst(222,"テストサイト2",date))
      when(mockSiteMstRepository.find(None)).thenReturn(list)

      val service = new DBService(mockSiteMstRepository)

      val result: List[GetSiteResponse] = service.getSite

      // 期待値
      val strDate = "%tY/%<tm/%<td %<tH:%<tM:%<tS" format date
      val expected = list.map(s => GetSiteResponse(s.siteId, s.siteName, strDate))
      result mustBe expected

    }
  }
}

trait Inject {
  lazy val injector = (new GuiceApplicationBuilder).injector()

  def inject[T : ClassTag]: T = injector.instanceOf[T]
}