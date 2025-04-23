package ru.gilgamesh.abon.userprofile.data.model

data class UserAchievementRemote (
    val registrationFlg: Boolean = false,
    val routeCreate1Flg: Boolean = false,
    val routeCreate2Flg: Boolean = false,
    val routeCreate3Flg: Boolean = false,
    val subscription1Flg: Boolean = false,
    val subscription2Flg: Boolean = false,
    val subscription3Flg: Boolean = false,
    val km1Flg: Boolean = false,
    val km2Flg: Boolean = false,
    val km3Flg: Boolean = false,
    val event1Flg: Boolean = false,
    val event2Flg: Boolean = false,
    val event3Flg: Boolean = false
)