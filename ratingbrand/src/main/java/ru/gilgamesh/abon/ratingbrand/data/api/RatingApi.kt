package ru.gilgamesh.abon.ratingbrand.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import ru.gilgamesh.abon.core.data.model.response.PageResponse
import ru.gilgamesh.abon.ratingbrand.data.model.response.BrandModelRatingResponse
import ru.gilgamesh.abon.ratingbrand.data.model.response.BrandRatingResponse

interface RatingApi {
    @GET("api/rating/getBrandRating")
    @Headers("Content-Type: application/json;charset=UTF-8")
    suspend fun getBrandRating(
        @Query("pageNumber") pageNumber: Int, @Query("pageSize") pageSize: Int?
    ): Response<PageResponse<BrandRatingResponse>>

    @GET("api/rating/getBrandModelRating")
    @Headers("Content-Type: application/json;charset=UTF-8")
    suspend fun getBrandModelRating(
        @Query("pageNumber") pageNumber: Int, @Query("pageSize") pageSize: Int?
    ): Response<PageResponse<BrandModelRatingResponse>>
}