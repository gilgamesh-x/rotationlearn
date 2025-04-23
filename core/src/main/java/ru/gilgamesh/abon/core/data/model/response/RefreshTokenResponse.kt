package ru.gilgamesh.abon.core.data.model.response

data class RefreshTokenResponse (
    val token: String,
    val refreshToken: String,
    val type: String,
    val tokenDurationMs: Long
)