package ru.gilgamesh.abon.motot.domain.repositories

import ru.gilgamesh.abon.motot.payload.response.auth.RefreshTokenResponse
import ru.gilgamesh.abon.motot.payload.response.auth.SigninResponse

interface AuthRepository {
    fun setLogin(login: String)
    fun setPassword(password: String)
    fun getRemember() : Boolean
    fun setRemember(remember: Boolean)
    fun setId(id: Long)
    fun setType(type: String)
    fun getType(): String
    fun setToken(token: String)
    fun getToken(): String
    fun setRefreshToken(refreshToken: String)
    fun getRefreshToken(): String
    fun getRoles(): Set<String>?
    fun setRoles(roles: Set<String>)
    fun getDuration(): Long
    fun getLoginDt(): Long
    fun saveAll(refreshResponse: RefreshTokenResponse)
    fun saveAll(signingResponse: SigninResponse)
}