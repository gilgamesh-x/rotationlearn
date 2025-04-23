package ru.gilgamesh.abon.userprofile.data.repository

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
import ru.gilgamesh.abon.core.data.model.response.IdentifierResponse
import ru.gilgamesh.abon.core.domain.repository.AuthRepository
import ru.gilgamesh.abon.userprofile.data.api.UserApi
import ru.gilgamesh.abon.userprofile.data.db.dao.UserDao
import ru.gilgamesh.abon.userprofile.data.db.dao.UserImageDao
import ru.gilgamesh.abon.userprofile.data.db.entity.UserEntity
import ru.gilgamesh.abon.userprofile.data.db.entity.UserImageEntity
import ru.gilgamesh.abon.userprofile.data.model.UserInfo
import ru.gilgamesh.abon.userprofile.data.model.request.ContactImageSaveRequest
import ru.gilgamesh.abon.userprofile.domain.repository.UserRepository
import java.io.File
import java.util.Base64
import java.util.stream.Collectors
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
                    userDao.insertUser(resultUserInfo.toUserEntity())
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
                        userDao.insertUser(resultUserInfo.toUserEntity())
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
        isDataLoadedFromApi = false
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
                                UserImageEntity(
                                    id = it.id, userId = contactId
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
                    emit(it.stream().map { IdentifierResponse(it.id) }.collect(Collectors.toList()))
                } ?: run {
                    emit(emptyList())
                }
            }
        }.onFailure {
            error(it.toString())
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun updateUser(data: UserEntity) {
        runCatching {
            userDao.updateUser(data)
            _userState.value = UserInfo(data)
        }
    }

    override fun getUserFromState(): UserInfo {
        return userState.value
    }

    override suspend fun updateAvatar(imgId: Long) {
        runCatching {
            userDao.updateAvatar(authRepository.getId(), imgId)
        }
    }

    override suspend fun updateMiniAvatar(imgId: Long) {
        runCatching {
            userDao.updateMiniAvatar(authRepository.getId(), imgId)
        }
    }

    override suspend fun updateCover(imgId: Long) {
        runCatching {
            userDao.updateCover(authRepository.getId(), imgId)
        }
    }

    override fun deletePhoto(id: Long): Flow<List<IdentifierResponse>> = flow {
        runCatching {
            val response = userApi.deleteImage(id)
            if (response.isSuccessful) {
                userImageDao.deleteImage(UserImageEntity(id, _userState.value.id))
                val responseGallery = userImageDao.getImagesByUserId(_userState.value.id)
                responseGallery?.let {
                    emit(it.stream().map { IdentifierResponse(it.id) }.collect(Collectors.toList()))
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
            val file = File(path)
            val tmp: ByteArray = FileUtils.readFileToByteArray(file)
            val contactImageSaveRequest = ContactImageSaveRequest(
                typeImg = "photo",
                imageData = Base64.getEncoder().encodeToString(tmp)
            )
            val response = userApi.saveImage(contactImageSaveRequest)
            if (response.isSuccessful) {
                response.body()?.id?.let {
                    userImageDao.insertImage(UserImageEntity(id = it, userId = _userState.value.id))
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