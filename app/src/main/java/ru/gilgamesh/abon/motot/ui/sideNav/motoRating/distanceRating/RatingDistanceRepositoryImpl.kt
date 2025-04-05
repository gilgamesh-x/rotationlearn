package ru.gilgamesh.abon.motot.ui.sideNav.motoRating.distanceRating

import ru.gilgamesh.abon.motot.data.api.RatingApi
import ru.gilgamesh.abon.motot.payload.response.rating.PageableRatingItem
import javax.inject.Inject

private const val PAGE_SIZE = 15
class RatingDistanceRepositoryImpl @Inject constructor(private val ratingApi: RatingApi) :
    RatingDistanceRepository {

    override suspend fun getDistanceRating(page: Int): PageableRatingItem? {
        runCatching {
            val response = ratingApi.getDistanceRating(page, PAGE_SIZE)
            return response.body()
        }
        return null
    }


    override suspend fun getDistanceRatingByYear(page: Int, year: Int): PageableRatingItem? {
        runCatching {
            val response = ratingApi.getDistanceRatingByYear(page, PAGE_SIZE, year)
            return response.body()
        }
        return null
    }
}