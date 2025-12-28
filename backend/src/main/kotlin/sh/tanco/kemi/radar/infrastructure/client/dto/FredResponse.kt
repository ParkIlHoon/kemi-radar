package sh.tanco.kemi.radar.infrastructure.client.dto

import kotlinx.serialization.Serializable

@Serializable
data class FredResponse(
    val observations: List<FredObservation>? = null
)

@Serializable
data class FredObservation(
    val value: String,
    val date: String
)