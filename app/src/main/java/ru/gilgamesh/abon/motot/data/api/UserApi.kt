package ru.gilgamesh.abon.motot.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query
import ru.gilgamesh.abon.motot.payload.request.contact.ContactImageSaveRequest
import ru.gilgamesh.abon.motot.payload.response.IdentifierResponse
import ru.gilgamesh.abon.motot.payload.response.MessageResponse
import ru.gilgamesh.abon.motot.payload.response.contact.UserInfoApi

interface UserApi {
    @GET("api/contact/getProfileInfo")
    @Headers("Content-Type: application/json;charset=UTF-8")
    suspend fun profileInfo(): Response<UserInfoApi?>

    @GET("api/getContactImageListId")
    @Headers("Content-Type: application/json;charset=UTF-8")
    suspend fun getContactImageListId(@Query("contactId") contactId: Long): Response<List<IdentifierResponse>>

    @DELETE("api/deleteImage")
    @Headers("Content-Type: application/json;charset=UTF-8")
    suspend fun deleteImage(@Query("imageId") imageId: Long): Response<MessageResponse>

    @POST("api/contactImage/saveImage")
    @Headers("Content-Type: application/json;charset=UTF-8")
    suspend fun saveImage(@Body body: ContactImageSaveRequest?): Response<IdentifierResponse>
}