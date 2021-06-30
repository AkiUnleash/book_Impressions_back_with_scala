package router

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model._
import controller.AccountController
import model.Account

import scala.concurrent.ExecutionContextExecutor

trait AccountRoutes extends SprayJsonSupport {
  //  AccountControllerの処理を使用できるように追加
  this: AccountController =>

  implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global

  val routes = {
    // 'http://localhost:8080/accounts'パス
    pathPrefix("accounts") {
      // '直下'
      pathEnd {
        post {
          entity(as[Account]) { account =>
            complete {
              create(account).map { result => HttpResponse(entity = "dog has been saved successfully") }
            }
          }
        }
      }
    }
  }
}