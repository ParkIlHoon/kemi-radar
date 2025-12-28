package sh.tanco.kemi.radar.infrastructure.client.exception

import io.ktor.http.HttpStatusCode

class EcosApiFailedException(url:String, httpStatus: HttpStatusCode): RuntimeException("""
    ECOS API 호출에 실패했습니다.
    HTTP 상태코드: $httpStatus
    요청 URL: $url
""".trimIndent())

class EcosApiServerException(url:String, code: String?, message: String?): RuntimeException("""
    ECOS API 서버에서 오류가 발생했습니다.
    오류 코드: $code
    오류 메시지: $message
    요청 URL: $url
""".trimIndent())

class EcosApiEmptyResultException(url:String): RuntimeException("""
    ECOS API 응답이 비어있습니다.
    요청 URL: $url
""".trimIndent())