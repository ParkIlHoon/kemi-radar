package sh.tanco.kemi.radar.domain.exchange

import java.time.LocalDate

interface WonDollarExchangeRateService {

    @Throws(ExchangeRateNotFoundException::class)
    suspend fun getExchangeRate(date: LocalDate): ExchangeRate
}