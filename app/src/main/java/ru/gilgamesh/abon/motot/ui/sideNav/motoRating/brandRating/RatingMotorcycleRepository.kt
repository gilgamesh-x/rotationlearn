package ru.gilgamesh.abon.motot.ui.sideNav.motoRating.brandRating

import androidx.lifecycle.LiveData
import ru.gilgamesh.abon.motot.ui.sideNav.motoRating.brandRating.recyclerViewRatingBrand.RatingMotorcycleItem

interface RatingMotorcycleRepository {
    fun observeBrandRating() : LiveData<List<RatingMotorcycleItem>>
    fun clear()
    suspend fun getBrandRatingFirst()
    suspend fun getBrandRatingNext()
    suspend fun getBrandModelRatingFirst()
    suspend fun getBrandModelRatingNext()
}