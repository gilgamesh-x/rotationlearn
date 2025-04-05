package ru.gilgamesh.abon.motot.ui.sideNav.motoRating.distanceRating

import ru.gilgamesh.abon.motot.payload.response.rating.PageableRatingItem
import ru.gilgamesh.abon.motot.ui.sideNav.motoRating.distanceRating.recycleViewRatingDistance.RatingItem

sealed class ItemResult {
    data class Loading(val data: List<RatingItem>?) : ItemResult()
    data class Success(val data: PageableRatingItem?) : ItemResult()
    data class Failure(val data: List<RatingItem>?, val throwable: Throwable) : ItemResult()
}