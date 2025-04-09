package ru.gilgamesh.abon.motot.ui.profile.enemyProfile

import ru.gilgamesh.abon.motot.payload.response.IdentifierResponse
import ru.gilgamesh.abon.motot.payload.response.chat.ChatContactResponse
import ru.gilgamesh.abon.motot.payload.response.contact.UserInfoApi

sealed class EnemyProfileEvent {
    sealed class Ui : EnemyProfileEvent() {
        object LoadProfile : Ui()
        object LoadUserImg : Ui()
        object LoadUserCoverImg : Ui()
        object LoadPhotoGallery : Ui()
        data class GotoFullSizeGallery(val position: Int): Ui()
        object ClickAvatar: Ui()
        object GoBack: Ui()
        object ClickShowMenu: Ui()
        object ClickComplain: Ui()
        object GotoAchievement: Ui()
        object ClickSubscribe: Ui()
        object ClickUnsubscribe: Ui()
        object ClickMessage: Ui()
    }

    sealed class Internal : EnemyProfileEvent() {
        data class ProfileLoadingSuccess(val value: UserInfoApi) : Internal()
        data class ProfileLoadingError(val throwable: Throwable) : Internal()
        data class LoadUserImgGlide(val imgId: Long) : Internal()
        data class LoadUserImgRes(val sex: String) : Internal()
        data class LoadUserCoverImgGlide(val imgId: Long) : Internal()
        object LoadUserCoverImgRes : Internal()
        data class GalleryIdsLoadingSuccess(val listIds: List<IdentifierResponse>) : Internal()
        data class GalleryIdsLoadingError(val throwable: Throwable) : Internal()
        object SubscribeSuccess : Internal()
        data class SubscribeError(val throwable: Throwable) : Internal()
        object UnsubscribeSuccess : Internal()
        data class UnsubscribeError(val throwable: Throwable) : Internal()
        data class GetChatSuccess(val chat: ChatContactResponse) : Internal()
        data class GetChatError(val throwable: Throwable) : Internal()

    }
}