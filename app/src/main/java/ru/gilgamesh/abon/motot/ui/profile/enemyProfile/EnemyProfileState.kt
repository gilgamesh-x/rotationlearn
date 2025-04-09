package ru.gilgamesh.abon.motot.ui.profile.enemyProfile

import ru.gilgamesh.abon.motot.payload.response.IdentifierResponse
import ru.gilgamesh.abon.motot.payload.response.contact.UserInfoApi

data class EnemyProfileState(
    val isLoadingProfile: Boolean = false,
    val value: UserInfoApi? = null,
    val isLoadingPhotoGallery: Boolean = false,
    val photoGalleryIds: List<IdentifierResponse> = emptyList(),
    var isChangedState: Boolean = false,
    var contactId: Long? = null,
    var notificationId: Long? = null,
    var prevActivity: String = "",
    var enableMsg: Boolean = false
)