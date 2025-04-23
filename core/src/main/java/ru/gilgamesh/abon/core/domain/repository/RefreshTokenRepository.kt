package ru.gilgamesh.abon.core.domain.repository

interface RefreshTokenRepository {
    fun isAliveToken() : Boolean
    fun refreshToken() : Boolean
}