package ru.gilgamesh.abon.userprofile.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "users_achievement",
    foreignKeys = [
    ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["id"],
        childColumns = ["id"],
        onDelete = ForeignKey.CASCADE
    )
])
data class UserAchievementEntity(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "reg_flg") val registrationFlg: Boolean = false,
    @ColumnInfo(name = "route_create_f_flg") val routeCreate1Flg: Boolean = false,
    @ColumnInfo(name = "route_create_s_flg") val routeCreate2Flg: Boolean = false,
    @ColumnInfo(name = "route_create_t_flg") val routeCreate3Flg: Boolean = false,
    @ColumnInfo(name = "route_subs_f_flg") val subscription1Flg: Boolean = false,
    @ColumnInfo(name = "route_subs_s_flg") val subscription2Flg: Boolean = false,
    @ColumnInfo(name = "route_subs_t_flg") val subscription3Flg: Boolean = false,
    @ColumnInfo(name = "route_km_f_flg") val km1Flg: Boolean = false,
    @ColumnInfo(name = "route_km_s_flg") val km2Flg: Boolean = false,
    @ColumnInfo(name = "route_km_t_flg") val km3Flg: Boolean = false,
    @ColumnInfo(name = "route_event_f_flg") val event1Flg: Boolean = false,
    @ColumnInfo(name = "route_event_s_flg") val event2Flg: Boolean = false,
    @ColumnInfo(name = "route_event_t_flg") val event3Flg: Boolean = false
)
