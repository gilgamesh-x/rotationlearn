package ru.gilgamesh.abon.motot.ui.sideNav.motoRating.distanceRating

sealed interface RatingDistanceLCEStateProfile<out T> {
    object Loading : RatingDistanceLCEStateProfile<Nothing>
    object Content : RatingDistanceLCEStateProfile<Nothing>
    data class Error(val message: String) : RatingDistanceLCEStateProfile<Nothing>
}