package ru.gilgamesh.abon.core.data.interceptors

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import ru.gilgamesh.abon.core.domain.repository.AuthRepository
import ru.gilgamesh.abon.core.domain.repository.RefreshTokenRepository
import java.io.IOException
import javax.inject.Inject

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
