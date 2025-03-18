package ru.gilgamesh.abon.motot.domain.repositories

interface RefreshTokenRepository {
    fun isAliveToken() : Boolean
    fun refreshToken() : Boolean
}