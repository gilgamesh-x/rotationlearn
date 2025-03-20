package ru.gilgamesh.abon.motot.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import ru.gilgamesh.abon.motot.data.api.AuthApi
import ru.gilgamesh.abon.motot.data.repository.AuthRepositoryImpl
import ru.gilgamesh.abon.motot.data.repository.RefreshTokenRepositoryImpl
import ru.gilgamesh.abon.motot.domain.repositories.AuthRepository
import ru.gilgamesh.abon.motot.domain.repositories.RefreshTokenRepository
import ru.gilgamesh.abon.motot.model.App
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {
    @Singleton
    @Provides
    @Named(value = "AuthSharedPref")
    fun provideAuthSharedPreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(App.USER_INFO, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(@Named("AuthSharedPref") sharedPreferences: SharedPreferences): AuthRepository {
        return AuthRepositoryImpl(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideRefreshTokenRepository(
        authRepository: AuthRepository,
        @Named(value = "RefreshToken") authApi: AuthApi
    ): RefreshTokenRepository {
        return RefreshTokenRepositoryImpl(authRepository, authApi)
    }

    @Provides
    @Singleton
    fun provideAuthApiMain(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    @Named(value = "LoginAuth")
    fun provideAuthApi(@Named(value = "RetrofitNonAuth") retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    @Named(value = "RefreshToken")
    fun provideAuthApiRefreshToken(@Named(value = "RetrofitRefreshToken") retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }
}