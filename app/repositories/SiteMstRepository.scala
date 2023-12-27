package repositories

import io.getquill.*
import models.SiteMst
import play.api.Logger

class SiteMstRepository {

  import db.OracleDB.oracleCtx
  import oracleCtx._

  private val logger = Logger(this.getClass)

  def find(id : Option[Long]) =

    // memo：これだとコンパイル時にクエリが出力されない
    // 参考：https://github.com/zio/zio-protoquill#migration-notes
    //    val q: Quoted[Query[SiteMst]] = quote {
    //      query[SiteMst].filter(_.siteId == lift(id))
    //    }
    // liftつけないとエラーになる
    inline def q: Quoted[Query[SiteMst]] =
      id match
        case Some(siteId) => quote {query[SiteMst].filter(_.siteId == lift(siteId))}
        case None => quote {query[SiteMst]}

    oracleCtx.run(q)

  def insert(site: SiteMst) =
    logger.info("repository start")

    inline def q: Quoted[Insert[SiteMst]] = quote {
      query[SiteMst].insertValue(lift(site))
    }
    oracleCtx.run(q)

  def update(site: SiteMst) =
    inline def q: Quoted[Update[SiteMst]] = quote {
      // これだと全項目更新される
      // query[SiteMst].filter(_.siteId == lift(site.siteId)).updateValue(lift(site))

      // これだと特定の項目のみ更新できる
      query[SiteMst].filter(_.siteId == lift(site.siteId)).update(_.siteName -> lift(site.siteName))
    }
    oracleCtx.run(q)

  def delete(siteId: Long) =
    inline def q: Quoted[Delete[SiteMst]] = quote {
      query[SiteMst].filter(_.siteId == lift(siteId)).delete
    }
    oracleCtx.run(q)

}