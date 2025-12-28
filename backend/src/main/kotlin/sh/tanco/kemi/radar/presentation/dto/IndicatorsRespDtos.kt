package sh.tanco.kemi.radar.presentation.dto

import java.time.LocalDate
import kotlinx.serialization.Serializable
import sh.tanco.kemi.radar.presentation.serializer.LocalDateSerializer

@Serializable
data class BondYieldRespDto(
    @Serializable(with = LocalDateSerializer::class)
    val date: LocalDate,
    val value: Double
)

@Serializable
data class ExchangeRateRespDto(
    @Serializable(with = LocalDateSerializer::class)
    val date: LocalDate,
    val value: Double
)

@Serializable
data class DollarIndexRespDto(
    @Serializable(with = LocalDateSerializer::class)
    val date: LocalDate,
    val value: Double
)

@Serializable
data class IndicatorsRespDto(
    val koreaGovBond10YearsYield: BondYieldRespDto,
    val usGovBond10YearsYield: BondYieldRespDto,
    val wonDollarExchangeRate: ExchangeRateRespDto,
    val dollarIndex: DollarIndexRespDto,
)