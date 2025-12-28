package sh.tanco.kemi.radar

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation as ServerContentNegotiation
import io.ktor.server.plugins.cors.routing.*
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import sh.tanco.kemi.radar.application.config.appModule
import sh.tanco.kemi.radar.infrastructure.config.ApiConfig
import sh.tanco.kemi.radar.infrastructure.config.infraModule
import sh.tanco.kemi.radar.presentation.routes.configureRouting

fun main() {
    embeddedServer(
        Netty,
        port = 8080,
        host = "0.0.0.0",
        module = Application::module
    ).start(wait = true)
}

fun Application.module() {
    // Configure plugins
    install(ServerContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }

    install(CORS) {
        anyHost()
        allowHeader("Content-Type")
    }

    install(Koin) {
        // Logging
        slf4jLogger()

        // ApiConfig를 환경 변수에서 읽어서 제공
        modules(module {
            single {
                ApiConfig(
                    ecosApiKey = System.getenv("ECOS_API_KEY")
                        ?: throw IllegalStateException("ECOS_API_KEY environment variable is required"),
                    fredApiKey = System.getenv("FRED_API_KEY")
                        ?: throw IllegalStateException("FRED_API_KEY environment variable is required")
                )
            }
        })

        modules(appModule, infraModule)
    }

    // Configure routing
    configureRouting()
}
