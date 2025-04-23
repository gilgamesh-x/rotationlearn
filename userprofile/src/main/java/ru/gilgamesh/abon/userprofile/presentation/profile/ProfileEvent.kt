package ru.gilgamesh.abon.userprofile.presentation.profile

import ru.gilgamesh.abon.core.data.model.response.IdentifierResponse
import ru.gilgamesh.abon.userprofile.data.model.UserInfo

sealed class ProfileEvent {
    sealed class Ui : ProfileEvent() {
        object LoadProfile : Ui()
        object LoadUserImg : Ui()
        object LoadUserCoverImg : Ui()
        object LoadPhotoGallery : Ui()
        object GotoListSubscribers : Ui()
        object GotoListSubscriptions : Ui()
        object GotoAddFriend : Ui()
        object GotoEditProfile : Ui()
        object GotoAchievement : Ui()
        object GotoFullAvatar : Ui()
        object ClickShowMenu : Ui()
        object ClickAddPhoto : Ui()
        data class GotoFullSizeGallery(val position: Int): Ui()
        data class DeletePhoto(val imgId: Long): Ui()
        data class AddPhoto(val path: String) : Ui()
    }

    sealed class Internal : ProfileEvent() {
        data class ProfileLoadingSuccess(val value: UserInfo) : Internal()
        data class ProfileLoadingError(val throwable: Throwable) : Internal()
        data class LoadUserImgGlide(val imgId: Long) : Internal()
        data class LoadUserImgRes(val sex: String) : Internal()
        data class LoadUserCoverImgGlide(val imgId: Long) : Internal()
        object LoadUserCoverImgRes : Internal()
        data class GalleryIdsLoadingSuccess(val listIds: List<IdentifierResponse>) : Internal()
        data class GalleryIdsLoadingError(val throwable: Throwable) : Internal()
        data class GalleryDeleteError(val throwable: Throwable) : Internal()
        data class AddPhotoSuccess(val imgId: Long) : Internal()
        data class AddPhotoError(val throwable: Throwable) : Internal()
    }
}