package controller

import database.MySQLDBImpl
import model.{Account, AccountTable}
import scala.concurrent.Future

trait AccountController extends AccountTable with MySQLDBImpl {

  import driver.api._

  protected val AccountTableQuery = TableQuery[AccountTable]

  // 起動時にスキーマを元にテーブルを作成する処理
  def ddl = db.run {
    AccountTableQuery.schema.create
  }

  // データ追加処理
  def create(account: Account): Future[Int] = db.run {
    AccountTableAutoInc += account
  }

  // アカウントからユーザー取得（ID指定）
  def getByUser(email: String): Future[Option[Account]] = db.run {
    AccountTableQuery.filter(_.email === email).result.headOption
  }

  def AccountTableAutoInc =
    AccountTableQuery returning AccountTableQuery.map(_.id)
}