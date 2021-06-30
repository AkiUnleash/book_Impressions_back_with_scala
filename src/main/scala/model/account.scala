package model

import database.MySQLDBImpl
import spray.json.DefaultJsonProtocol

// DefaultJsonProtocolを継承し、JSONを返すようにする
trait AccountTable extends DefaultJsonProtocol {
  // thisに指定することで、driverを使用に。
  this: MySQLDBImpl =>
  import driver.api._

  // ClassとJSONの変換。フォーマット定義
  implicit lazy val AccountFormat = jsonFormat2(Account)
  implicit lazy val AccountListFormat = jsonFormat1(AccountList)

  // テーブルスキーマの設定
  // 記述方法については以下を参照
  // http://krrrr38.github.io/slick-doc-ja/v3.0.out/%E3%82%B9%E3%82%AD%E3%83%BC%E3%83%9E.html
  class AccountTable(tag: Tag) extends Table[Account](tag, "account") {
    val id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    val name = column[String]("name")
    def * = (name, id.?) <>(Account.tupled, Account.unapply)
  }
}

// 使用するケースクラス(データ形式)
case class Account(name: String, id: Option[Int] = None)
case class AccountList(dogs: List[Account])