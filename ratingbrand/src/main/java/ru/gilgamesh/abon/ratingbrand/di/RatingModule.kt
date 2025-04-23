package ru.gilgamesh.abon.ratingbrand.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import ru.gilgamesh.abon.ratingbrand.data.api.RatingApi
import ru.gilgamesh.abon.ratingbrand.data.repository.RatingMotorcycleRepositoryImpl
import ru.gilgamesh.abon.ratingbrand.domain.RatingMotorcycleRepository
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RatingModule {
    @Provides
    @Singleton
    @Named(value = "RatingMotorcycle")
    fun provideRatingApi(retrofit: Retrofit): RatingApi {
        return retrofit.create(RatingApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRatingMotorcycleRepository(
        @Named(value = "RatingMotorcycle") api: RatingApi
    ): RatingMotorcycleRepository {
        return RatingMotorcycleRepositoryImpl(api)
    }
}