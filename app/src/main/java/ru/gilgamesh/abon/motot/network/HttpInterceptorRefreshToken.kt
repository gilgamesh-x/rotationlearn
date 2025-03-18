package ru.gilgamesh.abon.motot.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import ru.gilgamesh.abon.motot.domain.repositories.AuthRepository
import java.io.IOException
import javax.inject.Inject

class HttpInterceptorRefreshToken @Inject constructor(private val authRepository: AuthRepository) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original: Request = chain.request()
        val request = original.newBuilder()
            .header(AUTH_STR, authRepository.getType() + " " + authRepository.getToken())
            .addHeader("Connection", "close").build()
        val response: Response = chain.proceed(request)
        return response
    }

    companion object {
        private const val AUTH_STR = "Authorization"
    }
}
