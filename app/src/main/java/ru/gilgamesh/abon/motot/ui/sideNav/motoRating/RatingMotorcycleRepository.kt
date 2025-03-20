package ru.gilgamesh.abon.motot.ui.sideNav.motoRating

import androidx.lifecycle.LiveData
import ru.gilgamesh.abon.motot.ui.sideNav.motoRating.RVRatingMotorcycler.RatingMotorcycleItem

interface RatingMotorcycleRepository {
    fun observeBrandRating() : LiveData<List<RatingMotorcycleItem>>
    suspend fun getBrandRatingFirst()
    suspend fun getBrandRatingNext()
    suspend fun getBrandModelRatingFirst()
    suspend fun getBrandModelRatingNext()
}