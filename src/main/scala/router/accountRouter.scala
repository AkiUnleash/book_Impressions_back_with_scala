package router

import akka.http.scaladsl.server.Directives.{entity, _}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model._
import controller.AccountController
import model.{Account, AccountPost, LoginPost, UpdatePost}
import auth.{jwt, cookie}

import java.time.Clock
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

import java.util.UUID.randomUUID
import java.sql.Date
import scala.concurrent.{ExecutionContextExecutor, Future, Await}
import scala.concurrent.duration._

trait AccountRoutes extends SprayJsonSupport with jwt with cookie {
  //  AccountControllerの処理を使用できるように追加
  this: AccountController =>

  implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global
  implicit val bcrypt = new BCryptPasswordEncoder()

  val routes = {
    pathPrefix("account") {
      // Register Account
      path("signup") {
        post {
          entity(as[AccountPost]) { account =>
            val currentDate = new Date(System.currentTimeMillis())
            val accountPost = Account(
              account.username,
              account.email,
              bcrypt.encode(account.password),
              randomUUID.toString(),
              currentDate,
              currentDate,
              null
            )
            complete {
              create(accountPost).map { result => HttpResponse(entity = "Account has been saved successfully") }
            }
          }
        }
      } ~ path("login") {
        post {
          entity(as[LoginPost]) { account =>

            // E-mailからUUID及びPasswordを取り出す
            val accountData = getByUser(account.email)
            val password = Await.result(accountData.map(_.get.password), 1.second)
            val uuid = Await.result(accountData.map(_.get.uuid), 1.second)

            // Passwordを照合して一致すれば、Cookieに保存する。
            if(bcrypt.matches(account.password, password)) {
              val token = jwtEncode(uuid)
              settingCookie(token)
            } else {
              complete("NG!")
            }

          }
        }
      } ~ path("logout") {
        post { delCookie() }
      } ~ path("nowuser") {
        get {
            cookie("jwt") { nameCookie =>
              complete( getByUserUuid(jwtDecode(nameCookie.value))) }
        } ~
          post {
            entity(as[UpdatePost]) { account =>
              cookie("jwt") { nameCookie =>
                complete(
                  updateUser(jwtDecode(nameCookie.value), account).map { result => HttpResponse(entity = "account has been updated successfully") }
                )
              }
            }
          } ~
          delete {
            cookie("jwt") { nameCookie =>
              complete(
                deleteUser(jwtDecode(nameCookie.value)).map { result => HttpResponse(entity = "account has been delete successfully") }
              )
            }
          }
      }
    }
  }
}