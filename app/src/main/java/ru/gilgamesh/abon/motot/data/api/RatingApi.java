package ru.gilgamesh.abon.motot.data.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import ru.gilgamesh.abon.motot.payload.response.rating.PageableBrandModelRatingResponse;
import ru.gilgamesh.abon.motot.payload.response.rating.PageableBrandRatingResponse;
import ru.gilgamesh.abon.motot.payload.response.rating.PageableRatingItem;

public interface RatingApi {
    @GET("api/rating/getDistanceRating")
    @Headers("Content-Type: application/json;charset=UTF-8")
    Call<PageableRatingItem> getDistanceRating(@Query("pageNumber") Integer pageNumber, @Query("pageSize") Integer pageSize);

    @GET("api/rating/getDistanceRatingByYear")
    @Headers("Content-Type: application/json;charset=UTF-8")
    Call<PageableRatingItem> getDistanceRatingByYear(@Query("pageNumber") Integer pageNumber, @Query("pageSize") Integer pageSize, @Query("year") Integer year);

    @GET("api/rating/getBrandRating")
    @Headers("Content-Type: application/json;charset=UTF-8")
    Call<PageableBrandRatingResponse> getBrandRating(@Query("pageNumber") Integer pageNumber, @Query("pageSize") Integer pageSize);

    @GET("api/rating/getBrandModelRating")
    @Headers("Content-Type: application/json;charset=UTF-8")
    Call<PageableBrandModelRatingResponse> getBrandModelRating(@Query("pageNumber") Integer pageNumber, @Query("pageSize") Integer pageSize);
}
