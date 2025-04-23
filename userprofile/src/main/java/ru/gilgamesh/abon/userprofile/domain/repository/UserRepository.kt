package ru.gilgamesh.abon.userprofile.domain.repository

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import ru.gilgamesh.abon.core.data.model.response.IdentifierResponse
import ru.gilgamesh.abon.userprofile.data.db.entity.UserEntity
import ru.gilgamesh.abon.userprofile.data.model.UserInfo

interface UserRepository {
    fun getUserInfo(): Flow<UserInfo>
    fun getUserInfoApi(): LiveData<UserInfo>
    fun getPhotoGalleryIds(): Flow<List<IdentifierResponse>>
    suspend fun updateUser(data: UserEntity)
    fun getUserFromState() : UserInfo
    suspend fun updateAvatar(id: Long)
    suspend fun updateMiniAvatar(id: Long)
    suspend fun updateCover(id: Long)
    fun deletePhoto(id: Long): Flow<List<IdentifierResponse>>
    fun addPhoto(path: String): Flow<Long>
}