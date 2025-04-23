package ru.gilgamesh.abon.core.data.model.request

data class SignupRequest(
    val username: String,
    val email: String,
    val password: String,
    val nickname: String,
    val birtDate: String,
    val sex: String,
    val motoBrand: String,
    val motoModel: String,
    val userLocale: String,
    val isWantFindPair: Boolean,
    val firebaseToken: String,
    val firebaseTokenInstall: String
)
