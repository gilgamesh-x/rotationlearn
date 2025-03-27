package ru.gilgamesh.abon.motot.ui.sideNav.motoRating.distanceRating

sealed interface RatingDistanceLCEState <out T> {
    object Loading : RatingDistanceLCEState<Nothing>
    data class Content<out T>(val data: T) : RatingDistanceLCEState<T>
    data class Error(val message: String) : RatingDistanceLCEState<Nothing>
}