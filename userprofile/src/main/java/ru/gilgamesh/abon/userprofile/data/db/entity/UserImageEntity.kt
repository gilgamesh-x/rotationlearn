package ru.gilgamesh.abon.userprofile.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "user_images", foreignKeys = [ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["id"],
        childColumns = ["user_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class UserImageEntity(
    @PrimaryKey(autoGenerate = false) val id: Long = 0,
    @ColumnInfo(name = "user_id") val userId: Long
)