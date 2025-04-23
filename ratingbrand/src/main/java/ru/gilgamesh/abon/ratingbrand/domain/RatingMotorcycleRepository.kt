package ru.gilgamesh.abon.ratingbrand.domain

import androidx.lifecycle.LiveData
import ru.gilgamesh.abon.ratingbrand.presentation.recyclerViewRatingBrand.RatingMotorcycleItem

interface RatingMotorcycleRepository {
    fun observeBrandRating() : LiveData<List<RatingMotorcycleItem>>
    fun clear()
    suspend fun getBrandRatingFirst()
    suspend fun getBrandRatingNext()
    suspend fun getBrandModelRatingFirst()
    suspend fun getBrandModelRatingNext()
}