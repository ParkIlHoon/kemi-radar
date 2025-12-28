package sh.tanco.kemi.radar.domain.exchange

import java.time.LocalDate

data class ExchangeRate(
    val date: LocalDate,
    val rate: Double,
)

data class Index(
    val date: LocalDate,
    val value: Double,
)