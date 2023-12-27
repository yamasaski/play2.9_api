package repositories

import akka.stream.Materializer
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
import models.*
import play.api.Configuration
import play.api.db.Database
import play.api.db.Databases

import javax.inject.*
import com.typesafe.config.ConfigFactory

import tag.*

class SiteMstRepositorySpec extends PlaySpec with Results with GuiceOneAppPerSuite with MockitoSugar {

  def withMyDatabase[T](block: Database => T) = {

    val configuration = Configuration(ConfigFactory.load("test.conf"))

    Databases.withDatabase(
      driver = configuration.get[String]("test.oracle.driver"),
      url=configuration.get[String]("test.oracle.url"),
      config = Map(
        "username" -> configuration.get[String]("test.oracle.username"),
        "password" -> configuration.get[String]("test.oracle.password")
      )
    )(block)
  }

  "SiteMstRepository select" should {
    "find all" taggedAs DbSelect in {

      // DBから期待値を取得
      val expectedList = withMyDatabase { database =>
        val connection = database.getConnection()
        val rs = connection.prepareStatement("select * from SITE_MST").executeQuery()

        Iterator.continually(rs.next)
          .takeWhile(identity)
          .map { _ => SiteMst(rs.getLong(1), rs.getString(2), rs.getDate(3))}
          .toList
      }

      // 実行
      val repository = new SiteMstRepository
      val result: List[SiteMst] = repository.find(None)

      // 確認
      result mustBe expectedList
    }

    "find single" taggedAs DbSelect in {

      // DBから期待値を取得
      val expectedList = withMyDatabase { database =>
        val connection = database.getConnection()
        val rs = connection.prepareStatement("select * from SITE_MST where SITE_ID = 1111111").executeQuery()

        Iterator.continually(rs.next)
          .takeWhile(identity)
          .map { _ => SiteMst(rs.getLong(1), rs.getString(2), rs.getDate(3)) }
          .toList
      }

      // 実行
      val repository = new SiteMstRepository
      val result: List[SiteMst] = repository.find(Some(1111111))

      // 確認
      result mustBe expectedList
    }

  }

  "SiteMstRepository insert update delete" ignore {
    "insert" taggedAs DbUpdate in {
      // 引数
      val siteMst = SiteMst(958989, "単体テスト2")

      // 実行
      val repository = new SiteMstRepository
      val result: Long = repository.insert(siteMst)

      // 確認
      result mustBe 1
    }
  }
}