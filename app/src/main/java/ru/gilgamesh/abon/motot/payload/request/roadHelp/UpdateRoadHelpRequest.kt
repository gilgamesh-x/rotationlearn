package ru.gilgamesh.abon.motot.payload.request.roadHelp

class UpdateRoadHelpRequest (
    val id: Long,
    val reason: String,
    val comment: String,
    val phoneCountry: String,
    val phone: String
)