package ru.gilgamesh.abon.motot.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import ru.gilgamesh.abon.motot.payload.response.rating.PageableRatingItem

interface RatingApi {
    @GET("api/rating/getDistanceRating")
    @Headers("Content-Type: application/json;charset=UTF-8")
    suspend fun getDistanceRating(
        @Query("pageNumber") pageNumber: Int,
        @Query("pageSize") pageSize: Int?
    ): Response<PageableRatingItem?>

    @GET("api/rating/getDistanceRatingByYear")
    @Headers("Content-Type: application/json;charset=UTF-8")
    suspend fun getDistanceRatingByYear(
        @Query("pageNumber") pageNumber: Int,
        @Query("pageSize") pageSize: Int?,
        @Query("year") year: Int
    ): Response<PageableRatingItem?>
}
