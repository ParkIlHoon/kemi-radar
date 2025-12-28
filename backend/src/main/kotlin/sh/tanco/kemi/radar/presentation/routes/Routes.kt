package sh.tanco.kemi.radar.presentation.routes

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.async
import org.koin.ktor.ext.inject
import sh.tanco.kemi.radar.application.service.IndicatorService
import sh.tanco.kemi.radar.presentation.dto.BondYieldRespDto
import sh.tanco.kemi.radar.presentation.dto.DollarIndexRespDto
import sh.tanco.kemi.radar.presentation.dto.ExchangeRateRespDto
import sh.tanco.kemi.radar.presentation.dto.IndicatorsRespDto

fun Application.configureRouting() {

    val indicatorService by inject<IndicatorService>()

    routing {
        route("/api/indicators") {
            get("/bond-yields/korea-10years") {
                val data = indicatorService.getKoreaGovBond10YearsYield()
                with(data) {
                    call.respond(BondYieldRespDto(
                        date = date,
                        value = yield
                    ))
                }
            }

            get("/bond-yields/us-10years") {
                val data = indicatorService.getUsGovBond10YearsYield()
                with(data) {
                    call.respond(BondYieldRespDto(
                        date = date,
                        value = yield
                    ))
                }
            }

            get("/exchange-rates/krw-usd") {
                val data = indicatorService.wonDollarExchangeRate()
                with(data) {
                    call.respond(ExchangeRateRespDto(
                        date = date,
                        value = rate
                    ))
                }
            }

            get("/dollar-index") {
                val data = indicatorService.getDollarIndex()
                with(data) {
                    call.respond(DollarIndexRespDto(
                        date = date,
                        value = value
                    ))
                }
            }

            get("/all") {
                val koreaYieldDeferred = async { indicatorService.getKoreaGovBond10YearsYield() }
                val usYieldDeferred = async { indicatorService.getUsGovBond10YearsYield() }
                val exchangeRateDeferred = async { indicatorService.wonDollarExchangeRate() }
                val dollarIndexDeferred = async { indicatorService.getDollarIndex() }

                call.respond(IndicatorsRespDto(
                    koreaYieldDeferred.await().let { data -> BondYieldRespDto(data.date, data.yield) },
                    usYieldDeferred.await().let { data -> BondYieldRespDto(data.date, data.yield) },
                    exchangeRateDeferred.await().let { data -> ExchangeRateRespDto(data.date, data.rate) },
                    dollarIndexDeferred.await().let { data -> DollarIndexRespDto(data.date, data.value) },
                ))
            }

            get("/health") {
                call.respond(mapOf("status" to "OK"))
            }
        }
    }
}
