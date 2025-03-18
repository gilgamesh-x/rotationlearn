package ru.gilgamesh.abon.motot.payload.response.auth

import com.google.gson.Gson
import lombok.Getter
import lombok.Setter

class RefreshTokenResponse {
    val token: String? = null
    val refreshToken: String? = null
    val type: String? = null
    val tokenDurationMs: Long? = null

    override fun toString(): String {
        return Gson().toJson(this)
    }
}
