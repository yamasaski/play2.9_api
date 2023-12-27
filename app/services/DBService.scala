package services

import controllers.forms.SiteForm.*
import controllers.responses.*
import models.SiteMst
import play.api.Logger
import play.api.data.*
import play.api.i18n.*
import play.api.libs.json.Json
import play.api.mvc.*
import repositories.*

import javax.inject.*
import scala.concurrent.{ExecutionContext, Future}
import scala.language.postfixOps

@Singleton
class DBService @Inject()(siteMstRepository: SiteMstRepository) {

  import db.OracleDB.oracleCtx
  import oracleCtx._

  private val logger = Logger(this.getClass)

  def getSite: List[GetSiteResponse] =
    val list = siteMstRepository.find(None)
    // レスポンス作成
    list.map(s => GetSiteResponse(s.siteId, s.siteName, "%tY/%<tm/%<td %<tH:%<tM:%<tS" format s.registDate))


  def insertSite(form : SiteForm): Either[ErrorMessageResponse, InsertSiteResponse] =
    // トランザクション開始
    oracleCtx.transaction {
      // 存在チェック
      val list = siteMstRepository.find(Some(form.siteId))
      if (list.length > 0) {
        // 既にある場合はエラー
        Left(ErrorMessageResponse(Seq("存在するサイトIDです！")))
      } else {
        val siteMst = SiteMst(form.siteId, form.siteName)
        val result = siteMstRepository.insert(siteMst)
        val res = InsertSiteResponse(form.siteId, form.siteName, result)
        Right(res)
      }
    }

  def updateSite(form: SiteForm): Either[ErrorMessageResponse, InsertSiteResponse] =
    // トランザクション開始
    oracleCtx.transaction {
      // 存在チェック
      val list = siteMstRepository.find(Some(form.siteId))
      if (list.length == 0) {
        // 存在しない場合はエラー
        Left(ErrorMessageResponse(Seq("存在しないサイトIDです！")))
      } else {
        val siteMst = SiteMst(form.siteId, form.siteName)
        val result = siteMstRepository.update(siteMst)
        val res = InsertSiteResponse(form.siteId, form.siteName, result)
        Right(res)
      }
    }

  def deleteSite(siteId: Long): Either[ErrorMessageResponse, InsertSiteResponse] =
    // トランザクション開始
    oracleCtx.transaction {
      // 存在チェック
      val list = siteMstRepository.find(Some(siteId))
      if (list.length == 0) {
        // 存在しない場合はエラー
        Left(ErrorMessageResponse(Seq("存在しないサイトIDです！")))
      } else {
        val result = siteMstRepository.delete(siteId)
        val res = InsertSiteResponse(list.head.siteId, list.head.siteName, result)
        Right(res)
      }
    }

  def testTransaction: Long =
    val site = SiteMst(6666, "テストサイト")
    // 2回同じデータをinsertして一意制約エラーを起こす
    // 1回目のinsertがコミットされていないことを確認する
    oracleCtx.transaction {
      // 1回目は成功
      siteMstRepository.insert(site)
      // 2回目は失敗
      siteMstRepository.insert(site)
    }
}

