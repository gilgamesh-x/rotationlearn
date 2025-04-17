package ru.gilgamesh.abon.motot.ui.profile.userProfile

sealed class ProfileCommand {
    object LoadProfile : ProfileCommand()
    data class LoadUserImg(val imgId: Long?, val sex: String?) : ProfileCommand()
    data class LoadUserCoverImg(val imgId: Long?) : ProfileCommand()
    object LoadPhotoGallery : ProfileCommand()
    data class DeletePhoto(val imgId: Long) : ProfileCommand()
    data class AddPhoto(val path: String) : ProfileCommand()
}