package ru.gilgamesh.abon.motot.payload.response.newsfeed

import ru.gilgamesh.abon.motot.payload.response.route.RouteTrackingResponse

class NewsResponse : Cloneable {
    var id: Long? = null
    var imageId: String? = null
    var text: String? = null
    var likes: Int? = null
    var objectId: Long? = null
    var type: String? = null
    var created: String? = null
    var creatorId: Long? = null
    var creatorAvatarId: Long? = null
    var creatorNickname: String? = null
    var creatorSex: String? = null
    var verifiedFlg: Boolean? = null
    var likedFlg: Boolean? = null
    var route: RouteTrackingResponse? = null

    fun incrementLike() {
        likes = likes ?: 0
        likes = likes!! + 1
        likedFlg = true
    }

    fun decrementLike() {
        likes = likes ?: 0
        likes = likes!! - 1
        likedFlg = false
    }

    public override fun clone(): NewsResponse {
        return super.clone() as NewsResponse
    }
}
