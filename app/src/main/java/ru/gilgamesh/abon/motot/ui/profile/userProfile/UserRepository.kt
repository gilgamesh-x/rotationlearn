package ru.gilgamesh.abon.motot.ui.profile.userProfile

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import ru.gilgamesh.abon.motot.data.db.entity.User
import ru.gilgamesh.abon.motot.data.models.UserInfo
import ru.gilgamesh.abon.motot.payload.response.IdentifierResponse

interface UserRepository {
    fun getUserInfo(): Flow<UserInfo>
    fun getUserInfoApi(): LiveData<UserInfo>
    fun getPhotoGalleryIds(): Flow<List<IdentifierResponse>>
    suspend fun updateUser(data: User)
    fun getUserFromState() : UserInfo
    suspend fun updateAvatar(id: Long)
    suspend fun updateMiniAvatar(id: Long)
    suspend fun updateCover(id: Long)
    fun deletePhoto(id: Long): Flow<List<IdentifierResponse>>
    fun addPhoto(path: String): Flow<Long>
}