package ru.gilgamesh.abon.motot.ui.profile.enemyProfile

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.gilgamesh.abon.core.domain.repository.AuthRepository
import ru.gilgamesh.abon.motot.data.api.EnemyProfileApi
import ru.gilgamesh.abon.motot.payload.response.IdentifierResponse
import ru.gilgamesh.abon.motot.payload.response.chat.ChatContactResponse
import ru.gilgamesh.abon.motot.payload.response.contact.UserInfoApi
import ru.gilgamesh.abon.userprofile.data.db.dao.UserDao
import ru.gilgamesh.abon.userprofile.data.model.UserInfo
import javax.inject.Inject

class EnemyProfileRepositoryImpl @Inject constructor(
    private val contactApi: EnemyProfileApi,
    private val userDao: UserDao,
    private val authRepository: AuthRepository
) : EnemyProfileRepository {
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
        }.flowOn(Dispatchers.IO)

    override fun invokeSubscribe(contactId: Long?): Flow<Boolean> = flow {
        runCatching {
            contactId?.let {
                val response = contactApi.subscribe(contactId)
                if (response.isSuccessful) {
                    userDao.getUserFullById(authRepository.getId())?.apply {
                        val usr = UserInfo(this)
                        usr.countSubscriptions = usr.countSubscriptions?.let {
                            it + 1
                        } ?: 1
                        userDao.insertUser(usr.toUserEntity())
                    }
                }
                emit(response.isSuccessful)
            } ?: run {
                error("empty contactId")
            }
        }.onFailure {
            error(it.toString())
        }
    }.flowOn(Dispatchers.IO)

    override fun invokeUnsubscribe(contactId: Long?): Flow<Boolean> = flow {
        runCatching {
            contactId?.let {
                val response = contactApi.unsubscribe(contactId)
                if (response.isSuccessful) {
                    userDao.getUserFullById(authRepository.getId())?.apply {
                        val usr = UserInfo(this)
                        usr.countSubscriptions = usr.countSubscriptions?.let {
                            it - 1
                        } ?: 0
                        userDao.insertUser(usr.toUserEntity())
                    }
                }
                emit(response.isSuccessful)
            } ?: run {
                error("empty contactId")
            }
        }.onFailure {
            error(it.toString())
        }
    }.flowOn(Dispatchers.IO)

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
    }.flowOn(Dispatchers.IO)

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
    }.flowOn(Dispatchers.IO)
}