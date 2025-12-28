package sh.tanco.kemi.radar.domain.bond

import java.time.LocalDate
import kotlin.jvm.Throws

interface KoreaGovBondService {

    @Throws(BondYieldNotFoundException::class)
    suspend fun get10YearsYield(date: LocalDate): BondYield
}