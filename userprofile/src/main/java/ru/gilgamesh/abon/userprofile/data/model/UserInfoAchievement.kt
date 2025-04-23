package ru.gilgamesh.abon.userprofile.data.model

import ru.gilgamesh.abon.userprofile.data.db.entity.UserAchievementEntity

data class UserInfoAchievement (
    var registrationFlg: Boolean = false,
    var routeCreate1Flg: Boolean = false,
    var routeCreate2Flg: Boolean = false,
    var routeCreate3Flg: Boolean = false,
    var subscription1Flg: Boolean = false,
    var subscription2Flg: Boolean = false,
    var subscription3Flg: Boolean = false,
    var km1Flg: Boolean = false,
    var km2Flg: Boolean = false,
    var km3Flg: Boolean = false,
    var event1Flg: Boolean = false,
    var event2Flg: Boolean = false,
    var event3Flg: Boolean = false) {
    constructor(data: UserAchievementRemote?) : this() {
        registrationFlg = data?.registrationFlg ?: false
        routeCreate1Flg = data?.routeCreate1Flg ?: false
        routeCreate2Flg = data?.routeCreate2Flg ?: false
        routeCreate3Flg = data?.routeCreate3Flg ?: false
        subscription1Flg = data?.subscription1Flg ?: false
        subscription2Flg = data?.subscription2Flg ?: false
        subscription3Flg = data?.subscription3Flg ?: false
        km1Flg = data?.km1Flg ?: false
        km2Flg = data?.km2Flg ?: false
        km3Flg = data?.km3Flg ?: false
        event1Flg = data?.event1Flg ?: false
        event2Flg = data?.event2Flg ?: false
        event3Flg = data?.event3Flg ?: false
    }
    constructor(data: UserAchievementEntity) : this() {
        registrationFlg = data.registrationFlg
        routeCreate1Flg = data.routeCreate1Flg
        routeCreate2Flg = data.routeCreate2Flg
        routeCreate3Flg = data.routeCreate3Flg
        subscription1Flg = data.subscription1Flg
        subscription2Flg = data.subscription2Flg
        subscription3Flg = data.subscription3Flg
        km1Flg = data.km1Flg
        km2Flg = data.km2Flg
        km3Flg = data.km3Flg
        event1Flg = data.event1Flg
        event2Flg = data.event2Flg
        event3Flg = data.event3Flg
    }

    fun toUserAchievement(id: Long) :UserAchievementEntity {
        return UserAchievementEntity(
            id = id,
            registrationFlg = this.registrationFlg,
            routeCreate1Flg = this.routeCreate1Flg,
            routeCreate2Flg = this.routeCreate2Flg,
            routeCreate3Flg = this.routeCreate3Flg,
            subscription1Flg = this.subscription1Flg,
            subscription2Flg = this.subscription2Flg,
            subscription3Flg = this.subscription3Flg,
            km1Flg = this.km1Flg,
            km2Flg = this.km2Flg,
            km3Flg = this.km3Flg,
            event1Flg = this.event1Flg,
            event2Flg = this.event2Flg,
            event3Flg = this.event3Flg
        )
    }
}