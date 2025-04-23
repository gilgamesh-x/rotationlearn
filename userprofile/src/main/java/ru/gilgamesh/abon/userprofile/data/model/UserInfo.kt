package ru.gilgamesh.abon.userprofile.data.model

import ru.gilgamesh.abon.userprofile.data.db.dao.UserFull
import ru.gilgamesh.abon.userprofile.data.db.entity.UserEntity

data class UserInfo (var id: Long = -1) {
    var nickName: String? = null
    var birthDate: String? = null
    var age: Int? = null
    var about: String? = null
    var aboutPair: String? = null
    var countSubscriptions: Int? = null
    var countSubscribers: Int? = null
    var countMyRoute: Int? = null
    var countFinishRoute: Int? = null
    var countMyCompetition: Int? = null
    var countFinishCompetition: Int? = null
    var latitude: Double? = null
    var longitude: Double? = null
    var firstName: String? = null
    var sex: String? = null
    var motoBrand: String? = null
    var motoModel: String? = null
    var imRoadHelper: Boolean? = null
    var radiusRoadHelper: Int? = null
    var verifiedFlg: Boolean? = null
    var achievement: UserInfoAchievement = UserInfoAchievement()
    var distance: Double? = null
    var isWantFindPair: Boolean? = null
    var avatarId: Long? = null
    var miniAvatarId: Long? = null
    var coverId: Long? = null

    constructor(data: UserInfoRemote) : this() {
        id = data.id
        nickName = data.nickName
        birthDate = data.birthDate
        age = data.age
        about = data.about
        aboutPair = data.aboutPair
        countSubscribers = data.countSubscribers
        countSubscriptions = data.countSubscriptions
        countMyRoute = data.countMyRoute
        countFinishRoute = data.countFinishRoute
        countMyCompetition = data.countMyCompetition
        countFinishCompetition = data.countFinishCompetition
        latitude = data.latitude
        longitude = data.longitude
        firstName = data.firstName
        sex = data.sex
        motoBrand = data.motoBrand
        motoModel = data.motoModel
        imRoadHelper = data.imRoadHelper
        radiusRoadHelper = data.radiusRoadHelper
        verifiedFlg = data.verifiedFlg
        data.achievement.let {
            achievement = UserInfoAchievement(it)
        }
        distance = data.distance
        isWantFindPair = data.isWantFindPair
        avatarId = data.avatarId
        miniAvatarId = data.miniAvatarId
        coverId = data.coverId
    }

    constructor(data: UserEntity) : this() {
        id = data.id
        nickName = data.nickName
        birthDate = data.birthDate
        age = data.age
        about = data.about
        aboutPair = data.aboutPair
        countSubscribers = data.countSubscribers
        countSubscriptions = data.countSubscriptions
        countMyRoute = data.countMyRoute
        countFinishRoute = data.countFinishRoute
        countMyCompetition = data.countMyCompetition
        countFinishCompetition = data.countFinishCompetition
        latitude = data.latitude
        longitude = data.longitude
        firstName = data.firstName
        sex = data.sex
        motoBrand = data.motoBrand
        motoModel = data.motoModel
        imRoadHelper = data.imRoadHelper
        radiusRoadHelper = data.radiusRoadHelper
        verifiedFlg = data.verifiedFlg
        distance = data.distance
        isWantFindPair = data.isWantFindPair
        avatarId = data.avatarId
        miniAvatarId = data.miniAvatarId
        coverId = data.coverId
    }

    constructor(data: UserFull) : this() {
        id = data.user.id
        nickName = data.user.nickName
        birthDate = data.user.birthDate
        age = data.user.age
        about = data.user.about
        aboutPair = data.user.aboutPair
        countSubscribers = data.user.countSubscribers
        countSubscriptions = data.user.countSubscriptions
        countMyRoute = data.user.countMyRoute
        countFinishRoute = data.user.countFinishRoute
        countMyCompetition = data.user.countMyCompetition
        countFinishCompetition = data.user.countFinishCompetition
        latitude = data.user.latitude
        longitude = data.user.longitude
        firstName = data.user.firstName
        sex = data.user.sex
        motoBrand = data.user.motoBrand
        motoModel = data.user.motoModel
        imRoadHelper = data.user.imRoadHelper
        radiusRoadHelper = data.user.radiusRoadHelper
        verifiedFlg = data.user.verifiedFlg
        distance = data.user.distance
        isWantFindPair = data.user.isWantFindPair
        avatarId = data.user.avatarId
        miniAvatarId = data.user.miniAvatarId
        coverId = data.user.coverId
        data.achievement?.let {
            achievement = UserInfoAchievement(it)
        }
    }

    fun toUserEntity(): UserEntity {
        return UserEntity(
            id = this.id,
            nickName = this.nickName,
            birthDate = this.birthDate,
            age = this.age,
            about = this.about,
            aboutPair = this.aboutPair,
            countSubscribers = this.countSubscribers,
            countSubscriptions = this.countSubscriptions,
            countMyRoute = this.countMyRoute,
            countFinishRoute = this.countFinishRoute,
            countMyCompetition = this.countMyCompetition,
            countFinishCompetition = this.countFinishCompetition,
            latitude = this.latitude,
            longitude = this.longitude,
            firstName = this.firstName,
            sex = this.sex,
            motoBrand = this.motoBrand,
            motoModel = this.motoModel,
            imRoadHelper = this.imRoadHelper,
            radiusRoadHelper = this.radiusRoadHelper,
            verifiedFlg = this.verifiedFlg,
            distance = this.distance,
            isWantFindPair = this.isWantFindPair,
            avatarId = this.avatarId,
            miniAvatarId = this.miniAvatarId,
            coverId = this.coverId
        )
    }
}
