package ru.gilgamesh.abon.motot.ui.sideNav.motoRating.distanceRating

sealed class RatingDistanceLCEState <out T> (
    open val data: List<T> = emptyList()
) {
    data class Loading<out T>(
        override val data: List<T> = emptyList()
    ) : RatingDistanceLCEState<T>(data)

    data class Content<out T>(
        override val data: List<T>
    ) : RatingDistanceLCEState<T>(data)

    data class Error<out T>(
        override val data: List<T>, val message: String
    ) : RatingDistanceLCEState<T>(data)
}