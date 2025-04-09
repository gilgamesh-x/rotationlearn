package ru.gilgamesh.abon.motot.ui.profile.enemyProfile

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.gilgamesh.abon.motot.data.api.EnemyProfileApi
import ru.gilgamesh.abon.motot.payload.response.IdentifierResponse
import ru.gilgamesh.abon.motot.payload.response.chat.ChatContactResponse
import ru.gilgamesh.abon.motot.payload.response.contact.UserInfoApi
import javax.inject.Inject

class EnemyProfileRepositoryImpl @Inject constructor(private val contactApi: EnemyProfileApi) :
    EnemyProfileRepository {
    override fun loadEnemyProfileInfo(contactId: Long?, notificationId: Long?): Flow<UserInfoApi> =
        flow {
            runCatching {
                val response = contactApi.getEnemyProfileInfo(contactId, notificationId)
                if (response.isSuccessful) {
                    response.body()?.let {
                        emit(response.body()!!)
                    } ?: run {
                        error("getEnemyProfileInfo empty")
                    }
                } else {
                    error("getEnemyProfileInfo error")
                }
            }.onFailure {
                error(it.toString())
            }
        }

    override fun invokeSubscribe(contactId: Long?): Flow<Boolean> = flow {
        runCatching {
            contactId?.let {
                val response = contactApi.subscribe(contactId)
                emit(response.isSuccessful)
            } ?: run {
                error("empty contactId")
            }
        }.onFailure {
            error(it.toString())
        }
    }

    override fun invokeUnsubscribe(contactId: Long?): Flow<Boolean> = flow {
        runCatching {
            contactId?.let {
                val response = contactApi.unsubscribe(contactId)
                emit(response.isSuccessful)
            } ?: run {
                error("empty contactId")
            }
        }.onFailure {
            error(it.toString())
        }
    }

    override fun loadImageList(contactId: Long): Flow<List<IdentifierResponse>> = flow {
        runCatching {
            val response = contactApi.getContactImageListId(contactId)
            if (response.isSuccessful && response.body() != null) {
                emit(response.body()!!)
            } else {
                emit(emptyList())
            }
        }.onFailure {
            error(it.toString())
        }
    }

    override fun getChat(contactId: Long?): Flow<ChatContactResponse> = flow {
        runCatching {
            contactId?.let {
                val response = contactApi.getChatContact(contactId)
                if (response.isSuccessful) {
                    emit(response.body()!!)
                } else {
                    if (response.code() == 404) {
                        emit(ChatContactResponse())
                    } else {
                        error("!response.isSuccessful")
                    }
                }
            } ?: run {
                error("empty contactId")
            }
        }.onFailure {
            error(it.toString())
        }
    }
}