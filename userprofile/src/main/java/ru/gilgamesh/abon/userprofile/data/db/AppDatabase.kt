package ru.gilgamesh.abon.userprofile.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.gilgamesh.abon.userprofile.data.db.dao.UserDao
import ru.gilgamesh.abon.userprofile.data.db.dao.UserImageDao
import ru.gilgamesh.abon.userprofile.data.db.entity.UserAchievementEntity
import ru.gilgamesh.abon.userprofile.data.db.entity.UserEntity
import ru.gilgamesh.abon.userprofile.data.db.entity.UserImageEntity

@Database(
    entities = [UserEntity::class, UserImageEntity::class, UserAchievementEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun userImageDao(): UserImageDao
}