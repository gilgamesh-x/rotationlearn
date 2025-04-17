package ru.gilgamesh.abon.motot.ui.profile.userProfile

import ru.gilgamesh.abon.motot.data.models.UserInfo
import ru.gilgamesh.abon.motot.payload.response.IdentifierResponse

data class ProfileState (
    val isLoadingProfile: Boolean = false,
    val value: UserInfo? = null,
    val isLoadingPhotoGallery: Boolean = false,
    val photoGalleryIds: List<IdentifierResponse> = emptyList()
)