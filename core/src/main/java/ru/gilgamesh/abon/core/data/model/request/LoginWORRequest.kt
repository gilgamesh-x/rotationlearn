package ru.gilgamesh.abon.core.data.model.request

data class LoginWORRequest(
    val firebaseTokenInstall: String,
    val userLocale: String,
    val username: String,
    val password: String
)
