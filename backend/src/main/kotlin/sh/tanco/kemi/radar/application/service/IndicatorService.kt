package sh.tanco.kemi.radar.application.service

import sh.tanco.kemi.radar.domain.bond.BondYield
import sh.tanco.kemi.radar.domain.bond.BondYieldNotFoundException
import sh.tanco.kemi.radar.domain.bond.KoreaGovBondService
import sh.tanco.kemi.radar.domain.bond.UsGovBondService
import sh.tanco.kemi.radar.domain.exchange.DollarIndexNotFoundException
import sh.tanco.kemi.radar.domain.exchange.DollarIndexService
import sh.tanco.kemi.radar.domain.exchange.ExchangeRate
import sh.tanco.kemi.radar.domain.exchange.ExchangeRateNotFoundException
import sh.tanco.kemi.radar.domain.exchange.Index
import sh.tanco.kemi.radar.domain.exchange.WonDollarExchangeRateService
import java.time.LocalDate

class IndicatorService(
    private val koreaGovBondService: KoreaGovBondService,
    private val usGovBondService: UsGovBondService,
    private val dollarIndexService: DollarIndexService,
    private val wonDollarExchangeRateService: WonDollarExchangeRateService
) {

    suspend fun getKoreaGovBond10YearsYield(): BondYield {
        var currentDate = LocalDate.now()
        repeat(3) {
            try {
                return koreaGovBondService.get10YearsYield(currentDate)
            } catch (e: BondYieldNotFoundException) {
                currentDate = currentDate.minusDays(1)
            }
        }
        return BondYield(currentDate, 0.0)
    }

    suspend fun getUsGovBond10YearsYield(): BondYield = try {
        usGovBondService.get10YearsYield()
    } catch (e: BondYieldNotFoundException) {
        BondYield(LocalDate.now(), 0.0)
    }

    suspend fun getDollarIndex(): Index = try {
        dollarIndexService.getIndex()
    } catch (e: DollarIndexNotFoundException) {
        Index(date = LocalDate.now(), value = 0.0)
    }

    suspend fun wonDollarExchangeRate(): ExchangeRate {
        var currentDate = LocalDate.now()
        repeat(3) {
            try {
                return wonDollarExchangeRateService.getExchangeRate(currentDate)
            } catch (e: ExchangeRateNotFoundException) {
                currentDate = currentDate.minusDays(1)
            }
        }
        return ExchangeRate(currentDate, 0.0)
    }
}