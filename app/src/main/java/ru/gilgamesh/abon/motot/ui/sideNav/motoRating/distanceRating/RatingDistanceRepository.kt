package ru.gilgamesh.abon.motot.ui.sideNav.motoRating.distanceRating

import ru.gilgamesh.abon.motot.payload.response.rating.PageableRatingItem

interface RatingDistanceRepository {
    suspend fun getDistanceRating(page: Int): PageableRatingItem?
    suspend fun getDistanceRatingByYear(page: Int, year: Int): PageableRatingItem?
}