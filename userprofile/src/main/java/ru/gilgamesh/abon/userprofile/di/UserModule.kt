package ru.gilgamesh.abon.userprofile.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import ru.gilgamesh.abon.core.domain.repository.AuthRepository
import ru.gilgamesh.abon.userprofile.data.api.UserApi
import ru.gilgamesh.abon.userprofile.data.db.dao.UserDao
import ru.gilgamesh.abon.userprofile.data.db.dao.UserImageDao
import ru.gilgamesh.abon.userprofile.data.repository.UserRepositoryImpl
import ru.gilgamesh.abon.userprofile.domain.repository.UserRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserModule {

    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit): UserApi {
        return retrofit.create(UserApi::class.java)
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        userApi: UserApi,
        userDao: UserDao,
        userImageDao: UserImageDao,
        authRepository: AuthRepository
    ): UserRepository {
        return UserRepositoryImpl(userApi, userDao, userImageDao, authRepository)
    }
}