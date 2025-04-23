package ru.gilgamesh.abon.userprofile.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = false) val id: Long = 0,
    @ColumnInfo(name = "nickname") val nickName: String?,
    @ColumnInfo(name = "birthdate") val birthDate: String?,
    @ColumnInfo(name = "age") val age: Int?,
    @ColumnInfo(name = "about") val about: String?,
    @ColumnInfo(name = "about_pair") val aboutPair: String?,
    @ColumnInfo(name = "count_subscriptions") val countSubscriptions: Int?,
    @ColumnInfo(name = "count_subscribers") val countSubscribers: Int?,
    @ColumnInfo(name = "count_route_my") val countMyRoute: Int?,
    @ColumnInfo(name = "count_route_finish") val countFinishRoute: Int?,
    @ColumnInfo(name = "count_competition_my") val countMyCompetition: Int?,
    @ColumnInfo(name = "count_competition_finish") val countFinishCompetition: Int?,
    @ColumnInfo(name = "latitude") val latitude: Double?,
    @ColumnInfo(name = "longitude") val longitude: Double?,
    @ColumnInfo(name = "name_first") val firstName: String?,
    @ColumnInfo(name = "sex") val sex: String?,
    @ColumnInfo(name = "moto_brand") val motoBrand: String?,
    @ColumnInfo(name = "moto_model") val motoModel: String?,
    @ColumnInfo(name = "helper_flg") val imRoadHelper: Boolean?,
    @ColumnInfo(name = "helper_radius") val radiusRoadHelper: Int?,
    @ColumnInfo(name = "verified_flg") val verifiedFlg: Boolean?,
    @ColumnInfo(name = "distance") val distance: Double?,
    @ColumnInfo(name = "find_pair_flg") val isWantFindPair: Boolean?,
    @ColumnInfo(name = "avatar_id") val avatarId: Long?,
    @ColumnInfo(name = "avatar_mini_id") val miniAvatarId: Long?,
    @ColumnInfo(name = "cover_id") val coverId: Long?
)