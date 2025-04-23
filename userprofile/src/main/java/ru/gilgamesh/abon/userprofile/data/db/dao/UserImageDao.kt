package ru.gilgamesh.abon.userprofile.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.gilgamesh.abon.userprofile.data.db.entity.UserImageEntity

@Dao
interface UserImageDao {
    @Query("SELECT * FROM user_images WHERE user_id = :userId order by id desc")
    suspend fun getImagesByUserId(userId: Long): List<UserImageEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertImage(image: UserImageEntity)

    @Delete
    suspend fun deleteImage(image: UserImageEntity)
}