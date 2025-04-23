package ru.gilgamesh.abon.userprofile.presentation.profile

import ru.gilgamesh.abon.core.data.model.response.IdentifierResponse
import ru.gilgamesh.abon.userprofile.data.model.UserInfo

data class ProfileState (
    val isLoadingProfile: Boolean = false,
    val value: UserInfo? = null,
    val isLoadingPhotoGallery: Boolean = false,
    val photoGalleryIds: List<IdentifierResponse> = emptyList()
)