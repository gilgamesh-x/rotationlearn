package ru.gilgamesh.abon.motot.ui.sideNav.motoRating.distanceRating

data class RatingDistanceStateProfile(
    val contentState: RatingDistanceLCEStateProfile<*>? = null,
    var miniAvatarId: Long? = null,
    var sex: String? = null,
    var nickName: String = "",
    var motorcycle: String = "",
    var distance: Int = 0,
)