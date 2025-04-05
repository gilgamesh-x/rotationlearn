package ru.gilgamesh.abon.motot.ui.sideNav.motoRating.distanceRating

import ru.gilgamesh.abon.motot.ui.sideNav.motoRating.distanceRating.recycleViewRatingDistance.RatingItem

data class RatingDistanceState(
    val contentState: RatingDistanceLCEState<RatingItem> = RatingDistanceLCEState.Loading(),
    var yearMode: Int = 0,
    var currentPage: Int = 0,
    var isLoadingPage: Boolean = false
)