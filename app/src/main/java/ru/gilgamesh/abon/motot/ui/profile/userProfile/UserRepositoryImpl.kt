package ru.gilgamesh.abon.motot.ui.profile.userProfile

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.apache.commons.io.FileUtils
import ru.gilgamesh.abon.motot.data.api.UserApi
import ru.gilgamesh.abon.motot.data.db.dao.UserDao
import ru.gilgamesh.abon.motot.data.db.dao.UserImageDao
import ru.gilgamesh.abon.motot.data.db.entity.User
import ru.gilgamesh.abon.motot.data.db.entity.UserImage
import ru.gilgamesh.abon.motot.data.models.UserInfo
import ru.gilgamesh.abon.motot.domain.repositories.AuthRepository
import ru.gilgamesh.abon.motot.payload.request.contact.ContactImageSaveRequest
import ru.gilgamesh.abon.motot.payload.response.IdentifierResponse
import java.io.File
import java.util.Base64
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi,
    private val userDao: UserDao,
    private val userImageDao: UserImageDao,
    private val authRepository: AuthRepository
) : UserRepository {
    private val _userState: MutableStateFlow<UserInfo> = MutableStateFlow(UserInfo())
    val userState: StateFlow<UserInfo> = _userState.asStateFlow()

    private var isDataLoadedFromApi: Boolean = false
    private var isGalleryLoaded: Boolean = false

    override fun getUserInfo(): Flow<UserInfo> = flow {
        if (!isDataLoadedFromApi) {
            runCatching {
                val userFromRetrofit = userApi.profileInfo()
                if (userFromRetrofit.isSuccessful && userFromRetrofit.body() != null) {
                    val resultUserInfo = UserInfo(userFromRetrofit.body()!!)
                    userDao.insertUser(resultUserInfo.toUser())
                    resultUserInfo.achievement.let {
                        userDao.upsertUserAchievement(it.toUserAchievement(resultUserInfo.id))
                    }
                    _userState.value = resultUserInfo
                    isDataLoadedFromApi = true
                    emit(resultUserInfo)
                } else {
                    error("!isSuccessful")
                }
            }.onFailure { err ->
                val userFromRoom = userDao.getUserFullById(authRepository.getId())
                userFromRoom?.let {
                    emit(UserInfo(userFromRoom))
                } ?: run {
                    error(err.toString())
                }
            }
        } else {
            runCatching {
                val userFromRoom = userDao.getUserFullById(authRepository.getId())
                userFromRoom?.let {
                    emit(UserInfo(userFromRoom))
                } ?: run {
                    val userFromRetrofit = userApi.profileInfo()
                    if (userFromRetrofit.isSuccessful && userFromRetrofit.body() != null) {
                        val resultUserInfo = UserInfo(userFromRetrofit.body()!!)
                        userDao.insertUser(resultUserInfo.toUser())
                        resultUserInfo.achievement.let {
                            userDao.upsertUserAchievement(it.toUserAchievement(resultUserInfo.id))
                        }
                        _userState.value = resultUserInfo
                        isDataLoadedFromApi = true
                        emit(resultUserInfo)
                    } else {
                        error("!isSuccessful")
                    }
                }
            }.onFailure {
                error(it.toString())
            }
        }
    }.flowOn(Dispatchers.IO)

    override fun getUserInfoApi(): LiveData<UserInfo> {
        return getUserInfo().asLiveData()
    }

    override fun getPhotoGalleryIds(): Flow<List<IdentifierResponse>> = flow {
        runCatching {
            //TODO перевести полностью на использование бд и грузить только когда пусто, а не при перезапусках аппа
            var contactId: Long = authRepository.getId()
            if (!isGalleryLoaded) {
                val responseGallery = userApi.getContactImageListId(contactId)
                if (responseGallery.isSuccessful) {
                    isGalleryLoaded = true
                    responseGallery.body()?.let {
                        it.stream().forEach {
                            userImageDao.insertImage(
                                UserImage(
                                    id = it.id,
                                    userId = contactId
                                )
                            )
                        }
                        emit(it)
                    } ?: run {
                        emit(emptyList())
                    }
                } else {
                    error("!isSuccessful")
                }
            } else {
                val responseGallery = userImageDao.getImagesByUserId(contactId)
                responseGallery?.let {
                    emit(it.stream().map { IdentifierResponse(it.id) }.toList())
                } ?: run {
                    emit(emptyList())
                }
            }
        }.onFailure {
            error(it.toString())
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun updateUser(data: User) {
        runCatching {
            userDao.insertUser(data)
            _userState.value = UserInfo(data)
        }
    }

    override fun getUserFromState(): UserInfo {
        return userState.value
    }

    override suspend fun updateAvatar(id: Long) {
        runCatching {
            val userInfo = _userState.value
            userInfo.avatarId = id
            userDao.insertUser(userInfo.toUser())
        }
    }

    override suspend fun updateMiniAvatar(id: Long) {
        runCatching {
            val userInfo = _userState.value
            userInfo.miniAvatarId = id
            userDao.insertUser(userInfo.toUser())
        }
    }

    override suspend fun updateCover(id: Long) {
        runCatching {
            val userInfo = _userState.value
            userInfo.coverId = id
            userDao.insertUser(userInfo.toUser())
        }
    }

    override fun deletePhoto(id: Long): Flow<List<IdentifierResponse>> = flow {
        runCatching {
            val response = userApi.deleteImage(id)
            if (response.isSuccessful) {
                userImageDao.deleteImage(UserImage(id, _userState.value.id))
                val responseGallery = userImageDao.getImagesByUserId(_userState.value.id)
                responseGallery?.let {
                    emit(it.stream().map { IdentifierResponse(it.id) }.toList())
                } ?: run {
                    emit(emptyList())
                }
            } else {
                error("!isSuccessful")
            }
        }.onFailure {
            error(it.toString())
        }
    }.flowOn(Dispatchers.IO)

    override fun addPhoto(path: String): Flow<Long> = flow {
        runCatching {
            val contactImageSaveRequest = ContactImageSaveRequest()
            contactImageSaveRequest.typeImg = "photo"
            val file = File(path)
            val tmp: ByteArray = FileUtils.readFileToByteArray(file)
            contactImageSaveRequest.imageData = Base64.getEncoder().encodeToString(tmp)
            val response = userApi.saveImage(contactImageSaveRequest)
            if (response.isSuccessful) {
                response.body()?.id?.let {
                    userImageDao.insertImage(UserImage(id =  it, userId = _userState.value.id))
                    emit(it)
                } ?: {
                    error("body is null")
                }
            } else {
                error("!isSuccessful")
            }
        }.onFailure {
            error(it.toString())
        }
    }.flowOn(Dispatchers.IO)
}