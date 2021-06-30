package database

import slick.jdbc.MySQLProfile

// DB接続
trait MySQLDBImpl {
  val driver = MySQLProfile
  import driver.api.Database
  val db: Database = MySqlDB.connectionPool
}

// resources/application.confを参照する
private object MySqlDB {
  import slick.jdbc.MySQLProfile.api._
  val connectionPool = Database.forConfig("mysql")
}