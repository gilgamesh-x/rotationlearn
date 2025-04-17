package ru.gilgamesh.abon.motot.data.models

data class SaveRouteTrackRequest(
    val trackItems: List<TrackItemDto>,
    val type: String,
    val distance: Float,
    val averageSpeed: Int,
    val activeTime: Long,
    val totalTime: Long
)

data class TrackItemDto(val latitude: Double, val longitude: Double, val dateTime: String)
