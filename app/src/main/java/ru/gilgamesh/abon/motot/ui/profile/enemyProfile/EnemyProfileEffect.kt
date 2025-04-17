package ru.gilgamesh.abon.motot.ui.profile.enemyProfile

import ru.gilgamesh.abon.motot.payload.response.IdentifierResponse
import ru.gilgamesh.abon.motot.payload.response.contact.UserAchievementResponse
import ru.gilgamesh.abon.motot.payload.response.contact.UserInfoApi
import ru.gilgamesh.abon.motot.ui.profile.RecyclerViewImgGallery.ItemImg

sealed class EnemyProfileEffect {
    object ShowError : EnemyProfileEffect()
    data class ShowProfile(val data: UserInfoApi, val enableMsg: Boolean) : EnemyProfileEffect()
    data class LoadAvatarByGlide(val imgId: Long) : EnemyProfileEffect()
    data class LoadAvatarBySex(val sex: String) : EnemyProfileEffect()
    data class LoadCoverByGlide(val imgId: Long) : EnemyProfileEffect()
    object LoadCoverByRes : EnemyProfileEffect()
    data class LoadPhotoGallery(val data: List<ItemImg>) : EnemyProfileEffect()
    data class GotoFullSizeGallery(val data: List<IdentifierResponse>, val position: Int) :
        EnemyProfileEffect()

    data class GotoFullSizeAvatar(val imgId: Long?) : EnemyProfileEffect()
    object GotoBack : EnemyProfileEffect()
    object GotoBackRefresh : EnemyProfileEffect()
    object ShowMenu: EnemyProfileEffect()
    data class ShowComplain (val id: Long): EnemyProfileEffect()
    data class OpenAchievement (val ach: UserAchievementResponse): EnemyProfileEffect()
    data class Subscribed (val count: Int): EnemyProfileEffect()
    data class Unsubscribed (val count: Int): EnemyProfileEffect()
    data class GotoChat(val chatId: Long, val chatName: String) : EnemyProfileEffect()
    data class GotoChatNew(val companionId: Long, val chatName: String) : EnemyProfileEffect()
}