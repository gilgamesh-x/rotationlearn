package ru.gilgamesh.abon.motot.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query
import ru.gilgamesh.abon.motot.payload.response.IdentifierResponse
import ru.gilgamesh.abon.motot.payload.response.MessageResponse
import ru.gilgamesh.abon.motot.payload.response.chat.ChatContactResponse
import ru.gilgamesh.abon.motot.payload.response.contact.UserInfoApi

interface EnemyProfileApi {
    @GET("api/contact/getEnemyProfileInfo")
    @Headers("Content-Type: application/json;charset=UTF-8")
    suspend fun getEnemyProfileInfo(
        @Query("contactId") contactId: Long?, @Query("notificationId") notificationId: Long?
    ): Response<UserInfoApi>

    @POST("api/subscribe")
    @Headers("Content-Type: application/json;charset=UTF-8")
    suspend fun subscribe(@Query("contactId") contactId: Long): Response<MessageResponse?>

    @POST("api/unsubscribe")
    @Headers("Content-Type: application/json;charset=UTF-8")
    suspend fun unsubscribe(@Query("contactId") contactId: Long): Response<MessageResponse?>

    @GET("api/getContactImageListId")
    @Headers("Content-Type: application/json;charset=UTF-8")
    suspend fun getContactImageListId(@Query("contactId") contactId: Long): Response<List<IdentifierResponse>>

    @GET("api/getChatContact")
    @Headers("Content-Type: application/json;charset=UTF-8")
    suspend fun getChatContact(@Query("contactId") contactId: Long?): Response<ChatContactResponse>
}
