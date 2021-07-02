package auth

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives.{entity, _}
import akka.http.scaladsl.model.headers.HttpCookie

trait cookie {
  private val cookieName = "jwt"

  def settingCookie(token: String) = {
    setCookie(HttpCookie(cookieName, value = token, httpOnly = true)) {
      complete ("The user was logged in")
    }
  }

  def delCookie() = {
    deleteCookie(cookieName) {
      complete("The user was logged out")
    }
  }


}
