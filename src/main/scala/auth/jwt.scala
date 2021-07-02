package auth

import pdi.jwt.{Jwt, JwtAlgorithm}

trait jwt {
  private val securityKey = "SIIIII"

  def jwtEncode(data: String): String = {
    Jwt.encode(data, securityKey, JwtAlgorithm.HS256)
  }

  def jwtDecode (data: String): String = {
    val result = Jwt.decodeRaw(data, securityKey, Seq(JwtAlgorithm.HS256))
    val s"Success($uuid)" = result.toString()
    uuid
  }
}
