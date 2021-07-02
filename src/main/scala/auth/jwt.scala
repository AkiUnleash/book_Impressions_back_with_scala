package auth

import pdi.jwt.{Jwt, JwtAlgorithm}

trait jwt {
  private val securityKey = "SIIIII"

  def jwtEncode(data: String): String = {
    Jwt.encode(data, securityKey, JwtAlgorithm.HS256)
  }
}
