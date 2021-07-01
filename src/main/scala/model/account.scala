package model

import database.MySQLDBImpl
import spray.json.DefaultJsonProtocol
import spray.json.DeserializationException
import spray.json.JsString
import spray.json.JsValue
import spray.json.JsonFormat

import java.text.SimpleDateFormat
import java.time.{LocalDate, LocalDateTime}
import java.util.UUID
import java.sql.Date


// DefaultJsonProtocolを継承し、JSONを返すようにする
trait AccountTable extends DefaultJsonProtocol {
  // thisに指定することで、driverを使用に。
  this: MySQLDBImpl =>
  import driver.api._

  implicit object DateFormat extends JsonFormat[Date] {
    val formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
    def write(date: Date) = JsString(formatter.format(date))
    def read(value: JsValue) = {
      value match {
        case JsString(date) => new Date(formatter.parse(date).getTime())

        case _ => throw new DeserializationException("Expected JsString")
      }
    }
  }

  // ClassとJSONの変換。フォーマット定義
  implicit lazy val AccountFormat = jsonFormat7(Account)
  implicit lazy val AccountListFormat = jsonFormat1(AccountList)
  implicit lazy val AccountPostFormat = jsonFormat3(AccountPost)
  implicit lazy val LoginPostFormat = jsonFormat2(LoginPost)

  // テーブルスキーマの設定
  // 記述方法については以下を参照
  // http://krrrr38.github.io/slick-doc-ja/v3.0.out/%E3%82%B9%E3%82%AD%E3%83%BC%E3%83%9E.html
  class AccountTable(tag: Tag) extends Table[Account](tag, "accounts") {
    val id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    val uuid = column[String]("uuid")
    val username = column[String]("username")
    val email = column[String]("email")
    val password = column[String]("password")
    val createAt = column[Date]("create_at")
    val updateAt = column[Date]("update_at")

    def * = (username, email, password, uuid, createAt, updateAt, id.?) <>(Account.tupled, Account.unapply)
  }
}

// 使用するケースクラス(データ形式)
case class Account(username: String, email: String, password: String, uuid: String, createAt: Date, updateAt: Date, id: Option[Int] = None)
case class AccountList(accounts: List[Account])
case class AccountPost(username: String, email: String, password: String)
case class LoginPost(email: String, password: String)
