package ru.gilgamesh.abon.motot.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import ru.gilgamesh.abon.motot.data.api.UserApi
import ru.gilgamesh.abon.motot.data.db.app.AppDatabase
import ru.gilgamesh.abon.motot.data.db.dao.UserDao
import ru.gilgamesh.abon.motot.data.db.dao.UserImageDao
import ru.gilgamesh.abon.motot.domain.repositories.AuthRepository
import ru.gilgamesh.abon.motot.ui.profile.userProfile.UserRepository
import ru.gilgamesh.abon.motot.ui.profile.userProfile.UserRepositoryImpl
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

    @Provides
    @Singleton
    fun provideUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao()
    }

    @Provides
    @Singleton
    fun provideImageDao(appDatabase: AppDatabase): UserImageDao {
        return appDatabase.userImageDao()
    }
}