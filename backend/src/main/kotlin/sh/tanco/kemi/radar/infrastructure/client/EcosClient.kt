package sh.tanco.kemi.radar.infrastructure.client

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.serialization.json.Json
import sh.tanco.kemi.radar.domain.bond.BondYield
import sh.tanco.kemi.radar.domain.bond.BondYieldNotFoundException
import sh.tanco.kemi.radar.domain.bond.KoreaGovBondService
import sh.tanco.kemi.radar.domain.exchange.ExchangeRate
import sh.tanco.kemi.radar.domain.exchange.ExchangeRateNotFoundException
import sh.tanco.kemi.radar.domain.exchange.WonDollarExchangeRateService
import sh.tanco.kemi.radar.infrastructure.client.dto.EcosResponse
import sh.tanco.kemi.radar.infrastructure.client.dto.EcosRow
import sh.tanco.kemi.radar.infrastructure.client.exception.EcosApiEmptyResultException
import sh.tanco.kemi.radar.infrastructure.client.exception.EcosApiFailedException
import sh.tanco.kemi.radar.infrastructure.client.exception.EcosApiServerException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class EcosClient(
    private val ecosApiKey: String,
    private val httpClient: HttpClient
): KoreaGovBondService, WonDollarExchangeRateService {
    private val host = "https://ecos.bok.or.kr/api"

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    override suspend fun get10YearsYield(date: LocalDate): BondYield =
        try {
            val isoDate = date.format(DateTimeFormatter.BASIC_ISO_DATE)
            val url = "$host/StatisticSearch/$ecosApiKey/json/kr/1/1/817Y002/D/$isoDate/$isoDate/010210000"
            with(getEcosApiValue(url)) {
                BondYield(
                    date = LocalDate.parse(TIME, DateTimeFormatter.ofPattern("yyyyMMdd")),
                    yield = DATA_VALUE.toDouble(),
                )
            }
        } catch (e: EcosApiFailedException) {
            throw BondYieldNotFoundException(e)
        } catch (e: EcosApiServerException) {
            throw BondYieldNotFoundException(e)
        } catch (e: EcosApiEmptyResultException) {
            throw BondYieldNotFoundException(e)
        }

    override suspend fun getExchangeRate(date: LocalDate): ExchangeRate =
        try {
            val isoDate = date.format(DateTimeFormatter.BASIC_ISO_DATE)
            val url = "$host/StatisticSearch/$ecosApiKey/json/kr/1/1/731Y001/D/$isoDate/$isoDate/0000001"
            with(getEcosApiValue(url)) {
                ExchangeRate(
                    date = LocalDate.parse(TIME, DateTimeFormatter.ofPattern("yyyyMMdd")),
                    rate = DATA_VALUE.toDouble(),
                )
            }
        } catch (e: EcosApiFailedException) {
            throw ExchangeRateNotFoundException(e)
        } catch (e: EcosApiServerException) {
            throw ExchangeRateNotFoundException(e)
        } catch (e: EcosApiEmptyResultException) {
            throw ExchangeRateNotFoundException(e)
        }

    private suspend fun getEcosApiValue(url: String): EcosRow {
        val httpResponse = httpClient.get(url)
        if (!httpResponse.status.isSuccess()) {
            throw EcosApiFailedException(url, httpResponse.status)
        }

        val body: EcosResponse = json.decodeFromString<EcosResponse>(httpResponse.bodyAsText())
        if (body.isError()) {
            throw EcosApiServerException(url, body.RESULT?.CODE, body.RESULT?.MESSAGE)
        }
        if (body.StatisticSearch?.row.isNullOrEmpty()) {
            throw EcosApiEmptyResultException(url)
        }

        return body.StatisticSearch.row.last()
    }
}