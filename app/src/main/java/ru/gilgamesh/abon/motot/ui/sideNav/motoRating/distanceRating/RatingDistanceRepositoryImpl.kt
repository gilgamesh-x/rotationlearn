package ru.gilgamesh.abon.motot.ui.sideNav.motoRating.distanceRating

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gilgamesh.abon.motot.data.api.RatingApi
import ru.gilgamesh.abon.motot.ui.sideNav.motoRating.distanceRating.recycleViewRatingDistance.RatingItem
import javax.inject.Inject

class RatingDistanceRepositoryImpl @Inject constructor(private val ratingApi: RatingApi) :
    RatingDistanceRepository {

    companion object {
        const val PAGE_SIZE = 15
    }

    private var currentPage: Int = 0
    private var isLast: Boolean = false

    override suspend fun getRatingFirst(): List<RatingItem>? {
        isLast = false
        currentPage = 0
        return withContext(Dispatchers.IO) {
            return@withContext getDistanceRating()
        }
    }

    override suspend fun getRatingNext(): List<RatingItem>? {
        currentPage++
        return withContext(Dispatchers.IO) {
            return@withContext getDistanceRating()
        }
    }

    override suspend fun getRatingByYearFirst(year: Int): List<RatingItem>? {
        isLast = false
        currentPage = 0
        return withContext(Dispatchers.IO) {
            return@withContext getDistanceRatingByYear(year)
        }
    }

    override suspend fun getRatingByYearNext(year: Int): List<RatingItem>? {
        currentPage++
        return withContext(Dispatchers.IO) {
            return@withContext getDistanceRatingByYear(year)
        }
    }

    private suspend fun getDistanceRating(): List<RatingItem>? {
        if (isLast) return null
        runCatching {
            val response = ratingApi.getDistanceRating(currentPage, PAGE_SIZE)
            isLast = if (response.isSuccessful) {
                response.body()?.last ?: true
            } else {
                true
            }
            return response.body()?.content
        }.onFailure {
            Log.e("RatingDistanceRepository", it.toString())
            throw it
        }
        return null
    }


    private suspend fun getDistanceRatingByYear(year: Int): List<RatingItem>? {
        if (isLast) return null

        runCatching {
            val response = ratingApi.getDistanceRatingByYear(currentPage, PAGE_SIZE, year)
            isLast = if (response.isSuccessful) {
                response.body()?.last ?: true
            } else {
                true
            }
            return response.body()?.content
        }.onFailure {
            Log.e("RatingDistanceRepository", it.toString())
            throw it
        }
        return null
    }
}