package ru.gilgamesh.abon.userprofile.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import androidx.room.Update
import ru.gilgamesh.abon.userprofile.data.db.entity.UserAchievementEntity
import ru.gilgamesh.abon.userprofile.data.db.entity.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: Long): UserEntity?

    @Transaction
    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserFullById(userId: Long): UserFull?

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Transaction
    @Update
    suspend fun updateUser(user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertUserAchievement(userAchievement: UserAchievementEntity)

    @Delete
    suspend fun deleteUser(user: UserEntity)

    @Query("UPDATE users SET cover_id = :imgId WHERE id = :id")
    suspend fun updateCover(id: Long, imgId: Long): Int

    @Query("UPDATE users SET avatar_id = :imgId WHERE id = :id")
    suspend fun updateAvatar(id: Long, imgId: Long): Int

    @Query("UPDATE users SET avatar_mini_id = :imgId WHERE id = :id")
    suspend fun updateMiniAvatar(id: Long, imgId: Long): Int
}

data class UserFull(
    @Embedded val user: UserEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id"
    )
    val achievement: UserAchievementEntity?
)