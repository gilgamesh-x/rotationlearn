package ru.gilgamesh.abon.motot.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import ru.gilgamesh.abon.motot.data.api.RatingApi
import ru.gilgamesh.abon.motot.ui.sideNav.motoRating.RatingMotorcycleRepository
import ru.gilgamesh.abon.motot.ui.sideNav.motoRating.RatingMotorcycleRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RatingModule {
    @Provides
    @Singleton
    fun provideRatingApi(retrofit: Retrofit): RatingApi {
        return retrofit.create(RatingApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRatingMotorcycleRepository(api: RatingApi): RatingMotorcycleRepository {
        return RatingMotorcycleRepositoryImpl(api)
    }
}