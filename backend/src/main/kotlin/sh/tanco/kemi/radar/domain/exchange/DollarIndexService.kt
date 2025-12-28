package sh.tanco.kemi.radar.domain.exchange

import kotlin.jvm.Throws

interface DollarIndexService {

    @Throws(DollarIndexNotFoundException::class)
    suspend fun getIndex(): Index
}