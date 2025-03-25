package ru.gilgamesh.abon.motot.ui.sideNav.motoRating.distanceRating

import ru.gilgamesh.abon.motot.ui.sideNav.motoRating.distanceRating.recycleViewRatingDistance.RatingItem

interface RatingDistanceRepository {
    suspend fun getRatingFirst(): List<RatingItem>?
    suspend fun getRatingNext(): List<RatingItem>?
    suspend fun getRatingByYearFirst(year: Int): List<RatingItem>?
    suspend fun getRatingByYearNext(year: Int): List<RatingItem>?
}