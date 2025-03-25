package ru.gilgamesh.abon.motot.ui.sideNav.motoRating.distanceRating

import ru.gilgamesh.abon.motot.ui.sideNav.motoRating.distanceRating.recycleViewRatingDistance.RatingItem

data class RatingDistanceState(
    val items: List<RatingItem>? = emptyList(),
    val loading: Boolean = false,
    val error: String = ""
)
