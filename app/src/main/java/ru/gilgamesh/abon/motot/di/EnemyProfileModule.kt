package ru.gilgamesh.abon.motot.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import ru.gilgamesh.abon.motot.data.api.EnemyProfileApi
import ru.gilgamesh.abon.motot.ui.profile.enemyProfile.EnemyProfileRepository
import ru.gilgamesh.abon.motot.ui.profile.enemyProfile.EnemyProfileRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object EnemyProfileModule {
    @Provides
    @Singleton
    fun provideEnemyProfileApi(retrofit: Retrofit) : EnemyProfileApi {
        return retrofit.create(EnemyProfileApi::class.java)
    }

    @Provides
    @Singleton
    fun provideEnemyProfileRepository(enemyProfileApi: EnemyProfileApi) : EnemyProfileRepository {
        return EnemyProfileRepositoryImpl(enemyProfileApi)
    }
}