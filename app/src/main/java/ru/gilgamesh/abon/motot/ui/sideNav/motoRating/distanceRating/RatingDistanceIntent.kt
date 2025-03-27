package ru.gilgamesh.abon.motot.ui.sideNav.motoRating.distanceRating

sealed class RatingDistanceIntent {
    data object LoadFirstPageCurrentYear : RatingDistanceIntent()
    data object LoadNextPage : RatingDistanceIntent()
    data class LoadFirstPageByYear(val year: Int) : RatingDistanceIntent()
    data object LoadProfile : RatingDistanceIntent()
}
