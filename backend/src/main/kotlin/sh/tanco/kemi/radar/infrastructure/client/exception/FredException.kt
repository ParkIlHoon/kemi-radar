package sh.tanco.kemi.radar.infrastructure.client.exception

import io.ktor.http.HttpStatusCode

class FredApiFailedException(url:String, httpStatus: HttpStatusCode): RuntimeException("""
    FRED API 호출에 실패했습니다.
    HTTP 상태코드: $httpStatus
    요청 URL: $url
""".trimIndent())

class FredApiEmptyResultException(url:String): RuntimeException("""
    FRED API 응답이 비어있습니다.
    요청 URL: $url
""".trimIndent())