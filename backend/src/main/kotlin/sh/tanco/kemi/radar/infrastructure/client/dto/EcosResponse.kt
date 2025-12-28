package sh.tanco.kemi.radar.infrastructure.client.dto

import kotlinx.serialization.Serializable

@Serializable
data class EcosResponse(
    val StatisticSearch: StatisticSearch? = null,
    val RESULT: EcosError? = null
) {
    fun isError(): Boolean = StatisticSearch == null && RESULT != null
}

@Serializable
data class StatisticSearch(
    val row: List<EcosRow>? = null
)

@Serializable
data class EcosError(
    val CODE: String,
    val MESSAGE: String
)

@Serializable
data class EcosRow(
    val DATA_VALUE: String,
    val TIME: String
)