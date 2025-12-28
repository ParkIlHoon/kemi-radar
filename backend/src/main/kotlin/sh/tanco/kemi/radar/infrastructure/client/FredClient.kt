package sh.tanco.kemi.radar.infrastructure.client

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.http.isSuccess
import kotlinx.serialization.json.Json
import sh.tanco.kemi.radar.domain.bond.BondYield
import sh.tanco.kemi.radar.domain.bond.BondYieldNotFoundException
import sh.tanco.kemi.radar.domain.bond.UsGovBondService
import sh.tanco.kemi.radar.domain.exchange.DollarIndexNotFoundException
import sh.tanco.kemi.radar.domain.exchange.DollarIndexService
import sh.tanco.kemi.radar.domain.exchange.Index
import sh.tanco.kemi.radar.infrastructure.client.dto.FredObservation
import sh.tanco.kemi.radar.infrastructure.client.dto.FredResponse
import sh.tanco.kemi.radar.infrastructure.client.exception.FredApiEmptyResultException
import sh.tanco.kemi.radar.infrastructure.client.exception.FredApiFailedException
import java.time.LocalDate

class FredClient(
    private val fredApiKey: String,
    private val httpClient: HttpClient
): UsGovBondService, DollarIndexService {
    private val host = "https://api.stlouisfed.org/fred/series/observations"

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    override suspend fun get10YearsYield(): BondYield =
        try {
            with(getFredApiValue("DGS10")) {
                BondYield(
                    date = LocalDate.parse(date),
                    yield = value.toDouble()
                )
            }
        } catch (e: FredApiFailedException) {
            throw BondYieldNotFoundException(e)
        } catch (e: FredApiEmptyResultException) {
            throw BondYieldNotFoundException(e)
        }

    override suspend fun getIndex(): Index =
        try {
            with(getFredApiValue("DTWEXBGS")) {
                Index(
                    date = LocalDate.parse(date),
                    value = value.toDouble()
                )
            }
        } catch (e: FredApiFailedException) {
            throw DollarIndexNotFoundException(e)
        } catch (e: FredApiEmptyResultException) {
            throw DollarIndexNotFoundException(e)
        }

    private suspend fun getFredApiValue(seriesId: String): FredObservation {
        val httpResponse: HttpResponse = httpClient.get(host) {
            parameter("series_id", seriesId)
            parameter("api_key", fredApiKey)
            parameter("file_type", "json")
            parameter("limit", "1")
            parameter("sort_order", "desc")
        }
        if (!httpResponse.status.isSuccess()) {
            throw FredApiFailedException(httpResponse.request.url.toString(), httpResponse.status)
        }

        val body: FredResponse = json.decodeFromString<FredResponse>(httpResponse.bodyAsText())
        if (body.observations.isNullOrEmpty()) {
            throw FredApiEmptyResultException(httpResponse.request.url.toString())
        }

        return body.observations.first()
    }
}