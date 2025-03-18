package ru.gilgamesh.abon.motot.data.repository

import ru.gilgamesh.abon.motot.data.api.AuthApi
import ru.gilgamesh.abon.motot.domain.repositories.AuthRepository
import ru.gilgamesh.abon.motot.domain.repositories.RefreshTokenRepository
import ru.gilgamesh.abon.motot.payload.request.auth.TokenRefreshRequest
import java.util.Date
import javax.inject.Inject
import javax.inject.Named

class RefreshTokenRepositoryImpl @Inject constructor(
    private val authRepository: AuthRepository,
    @Named(value = "RefreshToken") val authApi: AuthApi
) : RefreshTokenRepository {


    override fun isAliveToken(): Boolean {
        val loginDate = authRepository.getLoginDt()
        val tokenDurationMs = authRepository.getDuration()
        if (loginDate == 0L) {
            return false
        }
        val dateLogin = Date(loginDate)
        val today = Date()
        val different = (today.time - dateLogin.time)
        return different < tokenDurationMs - 10000
    }

    override fun refreshToken(): Boolean {
        val responseCall =
        authApi.refreshAccessToken(TokenRefreshRequest(authRepository.getRefreshToken()))
        try {
            val tokenResponseResponse = responseCall.execute()
            if (tokenResponseResponse.isSuccessful) {
                tokenResponseResponse.body()?.let {
                    authRepository.saveAll(refreshResponse = it)
                }
            } else {
                return false
            }
        } catch (ex: Exception) {
            return false
        }
        return true
    }
}
