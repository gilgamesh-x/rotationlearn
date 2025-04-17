package ru.gilgamesh.abon.motot.ui.profile.userProfile

import ru.gilgamesh.abon.motot.data.models.UserInfo
import ru.gilgamesh.abon.motot.data.models.UserInfoAchievement
import ru.gilgamesh.abon.motot.payload.response.IdentifierResponse
import ru.gilgamesh.abon.motot.ui.profile.RecyclerViewImgGallery.ItemImg

sealed class ProfileEffect {
    object ShowError : ProfileEffect()
    data class ShowProfile(val data: UserInfo) : ProfileEffect()
    data class LoadAvatarByGlide(val imgId: Long) : ProfileEffect()
    data class LoadAvatarBySex(val sex: String) : ProfileEffect()
    data class LoadCoverByGlide(val imgId: Long) : ProfileEffect()
    object LoadCoverByRes : ProfileEffect()
    data class LoadPhotoGallery(val data: List<ItemImg>) : ProfileEffect()
    object LoadPhotoGalleryError : ProfileEffect()
    object GotoListSubscribers : ProfileEffect()
    object GotoListSubscriptions : ProfileEffect()
    object GotoEditProfile : ProfileEffect()
    object GotoAddFriend : ProfileEffect()
    data class GotoAchievement(val data: UserInfoAchievement) : ProfileEffect()
    data class GotoFullAvatar(val imgId: Long) : ProfileEffect()
    object ShowMenu : ProfileEffect()
    object GotoPickPhoto : ProfileEffect()
    data class GotoFullSizeGallery(val data: List<IdentifierResponse>, val position: Int) :
        ProfileEffect()
    object GalleryDeleteError : ProfileEffect()
    object GalleryAddError : ProfileEffect()
}