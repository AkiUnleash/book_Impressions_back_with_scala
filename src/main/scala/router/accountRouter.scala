package router

import akka.http.scaladsl.server.Directives.{entity, _}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.HttpCookie
import controller.AccountController
import model.{Account, AccountPost, LoginPost}

import java.time.Clock
import pdi.jwt._
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

import java.util.UUID.randomUUID
import java.sql.Date
import scala.concurrent.{ExecutionContextExecutor, Future, Await}
import scala.concurrent.duration._



trait AccountRoutes extends SprayJsonSupport {
  //  AccountControllerの処理を使用できるように追加
  this: AccountController =>

  implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global

  val routes = {
    pathPrefix("account") {
      // Register Account
      path("signup") {
        post {
          entity(as[AccountPost]) { account =>
            complete {
              val bcrypt = new BCryptPasswordEncoder()
              val currentDate = new Date(System.currentTimeMillis())
              val accountPost = Account(
                account.username,
                account.email,
                bcrypt.encode(account.password),
                randomUUID.toString(),
                currentDate,
                currentDate,
              )
              create(accountPost).map { result => HttpResponse(entity = "Account has been saved successfully") }
            }
          }
        }
      } ~ path("login") {
        post {
          entity(as[LoginPost]) { account =>
            val bcrypt = new BCryptPasswordEncoder()
            def authenticate(password: String): Boolean = {
              bcrypt.matches(account.password, password)
            }
            val ps = getByUser(account.email).map { _.get.password }
            val password = Await.result(ps, 1.second)
            if(authenticate(password)) {
              val token = Jwt.encode(account.password, "secretKey", JwtAlgorithm.HS256)
              setCookie(HttpCookie("jwt", value = token, httpOnly = true)) {
                complete("The user was logged in")
              }
            } else {
              complete("NG!")
            }
          }
        }
      } ~ path("logout") {
        post {
          deleteCookie("jwt") {
            complete("The user was logged out")
          }
        }
      } ~ path("test"){
        get {
          cookie("jwt") { token =>
            val name = Jwt.decodeRaw(token.value, "secretKey", Seq(JwtAlgorithm.HS256))
            complete(s"The logged in user is '${name}'")
          }
        } ~
        post {
          implicit val clock: Clock = Clock.systemUTC
          val token = Jwt.encode("akio", "secretKey", JwtAlgorithm.HS256)
          setCookie(HttpCookie("jwt", value = token, httpOnly = true)) {
            complete("The user was logged in")
          }
        } ~
          delete {
            deleteCookie("jwt") {
              complete("The user was logged out")
            }
          }
      }

    }
  }
}