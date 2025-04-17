package ru.gilgamesh.abon.motot.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import ru.gilgamesh.abon.motot.data.db.entity.User
import ru.gilgamesh.abon.motot.data.db.entity.UserAchievement

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: Long): User?

    @Transaction
    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserFullById(userId: Long): UserFull?

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertUserAchievement(userAchievement: UserAchievement)

    @Delete
    suspend fun deleteUser(user: User)
}

data class UserFull(
    @Embedded val user: User,
    @Relation(
        parentColumn = "id",
        entityColumn = "id"
    )
    val achievement: UserAchievement?
)