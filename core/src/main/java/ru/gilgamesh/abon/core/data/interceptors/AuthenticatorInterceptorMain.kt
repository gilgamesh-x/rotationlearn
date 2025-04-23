package ru.gilgamesh.abon.core.data.interceptors

import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import ru.gilgamesh.abon.core.domain.repository.AuthRepository
import ru.gilgamesh.abon.core.domain.repository.RefreshTokenRepository
import java.io.IOException
import javax.inject.Inject

class AuthenticatorInterceptorMain @Inject constructor(
    private val authRepository: AuthRepository,
    private val refreshTokenRepository: RefreshTokenRepository
) : Authenticator {

    @Throws(IOException::class)
    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.code == 401) {
            synchronized(this) {
                if (refreshTokenRepository.refreshToken()) {
                    return response.request.newBuilder().header(
                        AUTH_STR, authRepository.getType() + " " + authRepository.getToken()
                    ).addHeader("Connection", "close").build()
                }
            }
        }
        return null
    }

    companion object {
        private const val AUTH_STR = "Authorization"
    }
}
