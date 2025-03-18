package ru.gilgamesh.abon.motot.payload.request.roadHelp

class AddRoadHelpRequest (
    val reason: String,
    val comment: String,
    val phoneCountry: String,
    val phone: String
)