package repositories.db

import io.getquill.*

object MysqlDB {
  lazy val mySqlCtx = new MysqlJdbcContext(SnakeCase, "mySqlDb")
}
