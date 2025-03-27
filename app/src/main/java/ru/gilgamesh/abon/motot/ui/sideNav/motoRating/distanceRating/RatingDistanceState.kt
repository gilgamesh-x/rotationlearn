package ru.gilgamesh.abon.motot.ui.sideNav.motoRating.distanceRating

import ru.gilgamesh.abon.motot.ui.sideNav.motoRating.distanceRating.recycleViewRatingDistance.RatingItem

data class RatingDistanceState(
    val contentState: RatingDistanceLCEState<List<RatingItem>?>? = null,
    var yearMode: Int = 0
)