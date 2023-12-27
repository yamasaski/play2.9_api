package controllers

import java.io.*
import java.nio.file.Files
import scala.concurrent.Future
import akka.stream.scaladsl.*
import akka.util.ByteString
import org.scalatestplus.play.*
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import play.api.libs.json.JsValue
import play.api.libs.ws.WSClient
import play.api.mvc.*
import play.api.test.Helpers.*
import play.api.test.*
import play.api.libs.ws.DefaultBodyReadables.readableAsString

import concurrent.ExecutionContext.Implicits.global

import tag.*

class FileControllerSpec extends PlaySpec with GuiceOneServerPerSuite with Injecting {

  "upload" should {
    "アップロード成功" in {
      val tmpFile = java.io.File.createTempFile("prefix", "txt")
      tmpFile.deleteOnExit()
      val msg = "hello world"
      Files.write(tmpFile.toPath, msg.getBytes())

      val url = s"http://localhost:${port}/upload"
      val responseFuture = inject[WSClient].url(url).post(postSource(tmpFile))
      val response = await(responseFuture)
      response.status mustBe OK
      response.body mustBe "file size = 11"
    }
  }

  "download" should {
    "ダウンロード成功" in {
      val controller = new FileController(Helpers.stubMessagesControllerComponents())
      val result: Future[Result] = controller.download.apply(FakeRequest())
      // TODO contentAsBytesでファイルに変換して結果確認しようと思ったけどなんか無理っぽい
      val resContentType: Option[String] = contentType(result)
      println("resContentType：" + resContentType)
      resContentType mustBe Some("application/octet-stream")
    }
  }

  def postSource(tmpFile: File): Source[MultipartFormData.Part[Source[ByteString, _]], _] = {
    import play.api.mvc.MultipartFormData._
    Source(FilePart("name", "hello.txt", Option("text/plain"),
      FileIO.fromPath(tmpFile.toPath)) :: DataPart("key", "value") :: List())
  }
}