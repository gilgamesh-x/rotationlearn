package ru.gilgamesh.abon.motot.data.db.app

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.gilgamesh.abon.motot.data.db.dao.UserDao
import ru.gilgamesh.abon.motot.data.db.dao.UserImageDao
import ru.gilgamesh.abon.motot.data.db.entity.User
import ru.gilgamesh.abon.motot.data.db.entity.UserAchievement
import ru.gilgamesh.abon.motot.data.db.entity.UserImage

@Database(entities = [User::class, UserImage::class, UserAchievement::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun userImageDao(): UserImageDao
}