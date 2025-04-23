package ru.gilgamesh.abon.core.data.model.response

data class SigninResponse (
    val token: String,
    val type: String,
    val refreshToken: String,
    val id: Long,
    val roles: Set<String>,
    val dateLoginLong: Long,
    val tokenDurationMs: Long,
    val contactId: Long,
)