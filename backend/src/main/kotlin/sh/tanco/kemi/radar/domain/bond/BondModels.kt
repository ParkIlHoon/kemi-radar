package sh.tanco.kemi.radar.domain.bond

import java.time.LocalDate

data class BondYield(
    val date: LocalDate,
    val yield: Double,
)
