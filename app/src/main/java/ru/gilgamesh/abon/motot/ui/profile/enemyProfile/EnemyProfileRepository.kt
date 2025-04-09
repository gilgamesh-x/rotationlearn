package ru.gilgamesh.abon.motot.ui.profile.enemyProfile

import kotlinx.coroutines.flow.Flow
import ru.gilgamesh.abon.motot.payload.response.IdentifierResponse
import ru.gilgamesh.abon.motot.payload.response.chat.ChatContactResponse
import ru.gilgamesh.abon.motot.payload.response.contact.UserInfoApi

interface EnemyProfileRepository {
    fun loadEnemyProfileInfo(contactId: Long?, notificationId: Long?): Flow<UserInfoApi>
    fun invokeSubscribe(contactId: Long?): Flow<Boolean>
    fun invokeUnsubscribe(contactId: Long?): Flow<Boolean>
    fun loadImageList(contactId: Long): Flow<List<IdentifierResponse>>
    fun getChat(contactId: Long?): Flow<ChatContactResponse>
}