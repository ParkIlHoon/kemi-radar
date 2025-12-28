package sh.tanco.kemi.radar.infrastructure.config

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import sh.tanco.kemi.radar.domain.bond.KoreaGovBondService
import sh.tanco.kemi.radar.domain.bond.UsGovBondService
import sh.tanco.kemi.radar.domain.exchange.DollarIndexService
import sh.tanco.kemi.radar.domain.exchange.WonDollarExchangeRateService
import sh.tanco.kemi.radar.infrastructure.client.EcosClient
import sh.tanco.kemi.radar.infrastructure.client.FredClient

val infraModule = module {
    single<HttpClient> { HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.INFO
        }
    } }

    // API 클라이언트 생성
    single { EcosClient(get<ApiConfig>().ecosApiKey, get()) }
    single { FredClient(get<ApiConfig>().fredApiKey, get()) }

    /*
     * Domain 인터페이스 바인딩
     */
    single<KoreaGovBondService> { get<EcosClient>() }
    single<WonDollarExchangeRateService> { get<EcosClient>() }
    single<UsGovBondService> { get<FredClient>() }
    single<DollarIndexService> { get<FredClient>() }
}