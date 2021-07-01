package router

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model._
import controller.AccountController
import model.{Account, AccountPost}

import java.util.UUID.randomUUID

import scala.concurrent.ExecutionContextExecutor

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
              val accountPost = Account(
                account.username,
                account.email,
                account.password,
                randomUUID.toString()
              )
              create(accountPost).map { result => HttpResponse(entity = "dog has been saved successfully") }
            }
          }
        }
      }

    }
  }
}