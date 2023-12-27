package repositories.db

import io.getquill.*

object OracleDB {
  lazy val oracleCtx = new OracleJdbcContext(SnakeCase, "oracleDb")
}
