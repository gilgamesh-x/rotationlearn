package ru.gilgamesh.abon.userprofile.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.gilgamesh.abon.userprofile.data.db.AppDatabase
import ru.gilgamesh.abon.userprofile.data.db.dao.UserDao
import ru.gilgamesh.abon.userprofile.data.db.dao.UserImageDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(
            application, AppDatabase::class.java, "mh_user_db"
        ).fallbackToDestructiveMigration(true).build()
    }

    @Provides
    @Singleton
    fun provideUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao()
    }

    @Provides
    @Singleton
    fun provideImageDao(appDatabase: AppDatabase): UserImageDao {
        return appDatabase.userImageDao()
    }
}