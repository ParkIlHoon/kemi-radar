package sh.tanco.kemi.radar.domain.bond

interface UsGovBondService {

    @Throws(BondYieldNotFoundException::class)
    suspend fun get10YearsYield(): BondYield
}