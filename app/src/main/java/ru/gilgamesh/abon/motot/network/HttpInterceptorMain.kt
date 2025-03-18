package ru.gilgamesh.abon.motot.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import ru.gilgamesh.abon.motot.data.api.AuthApi
import ru.gilgamesh.abon.motot.domain.repositories.AuthRepository
import ru.gilgamesh.abon.motot.domain.repositories.RefreshTokenRepository
import java.io.IOException
import javax.inject.Inject
import javax.inject.Named

class HttpInterceptorMain @Inject constructor(
    private val authRepository: AuthRepository,
    private val refreshTokenRepository: RefreshTokenRepository
) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original: Request = chain.request()
        synchronized(this) {
            if (!refreshTokenRepository.isAliveToken()) {
                refreshTokenRepository.refreshToken()
            }
        }
        val request = original.newBuilder()
            .header(AUTH_STR, authRepository.getType() + " " + authRepository.getToken())
            .addHeader("Connection", "close").build()
        val response: Response = chain.proceed(request)

        if (response.code == 401) {
            Log.d(TAG, "intercept: 401")
        }
        return response
    }

    companion object {
        private val TAG: String = HttpInterceptorMain::class.java.simpleName
        private const val AUTH_STR = "Authorization"
    }
}
