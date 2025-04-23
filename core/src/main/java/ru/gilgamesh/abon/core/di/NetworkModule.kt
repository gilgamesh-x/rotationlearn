package ru.gilgamesh.abon.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.gilgamesh.abon.core.BuildConfig
import ru.gilgamesh.abon.core.data.interceptors.AuthenticatorInterceptorMain
import ru.gilgamesh.abon.core.data.interceptors.HttpInterceptorMain
import ru.gilgamesh.abon.core.data.interceptors.HttpInterceptorRefreshToken
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(BuildConfig.HOST_MICRO_URL)
            .addConverterFactory(GsonConverterFactory.create()).client(okHttpClient).build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        interceptor: HttpInterceptorMain,
        authenticator: AuthenticatorInterceptorMain
    ): OkHttpClient {
        val okHttpClient: OkHttpClient.Builder =
            OkHttpClient.Builder().addInterceptor(interceptor).authenticator(authenticator)
                .callTimeout(30, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG) {
            okHttpClient.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        }
        return okHttpClient.build()
    }

    @Provides
    @Singleton
    @Named(value = "RetrofitNonAuth")
    fun provideRetrofitNonAuth(@Named(value = "OkHttpClientNonAuth") okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(BuildConfig.HOST_MICRO_URL)
            .addConverterFactory(GsonConverterFactory.create()).client(okHttpClient).build()
    }

    @Provides
    @Singleton
    @Named(value = "OkHttpClientNonAuth")
    fun provideOkHttpClientNonAuth(): OkHttpClient {
        val okHttpClient: OkHttpClient.Builder =
            OkHttpClient.Builder().callTimeout(30, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG) {
            okHttpClient.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        }
        return okHttpClient.build()
    }

    @Provides
    @Singleton
    @Named(value = "RetrofitRefreshToken")
    fun provideRetrofitRefreshToken(@Named(value = "OkHttpClientRefreshToken") okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(BuildConfig.HOST_MICRO_URL)
            .addConverterFactory(GsonConverterFactory.create()).client(okHttpClient).build()
    }

    @Provides
    @Singleton
    @Named(value = "OkHttpClientRefreshToken")
    fun provideOkHttpClientRefreshToken(interceptor: HttpInterceptorRefreshToken): OkHttpClient {
        val okHttpClient: OkHttpClient.Builder =
            OkHttpClient.Builder().addInterceptor(interceptor).callTimeout(30, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG) {
            okHttpClient.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        }
        return okHttpClient.build()
    }
}