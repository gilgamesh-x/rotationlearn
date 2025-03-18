package ru.gilgamesh.abon.motot.network

import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import ru.gilgamesh.abon.motot.domain.repositories.AuthRepository
import ru.gilgamesh.abon.motot.domain.repositories.RefreshTokenRepository
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
