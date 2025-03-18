package ru.gilgamesh.abon.motot.ui.sideNav.motoRating

import androidx.lifecycle.LiveData
import ru.gilgamesh.abon.motot.ui.sideNav.motoRating.RVRatingMotorcycler.RatingMotorcycleItem

interface RatingMotorcycleRepository {
    fun observeBrandRating() : LiveData<List<RatingMotorcycleItem>>
    fun getBrandRatingFirst()
    fun getBrandRatingNext()
    fun getBrandModelRatingFirst()
    fun getBrandModelRatingNext()
}