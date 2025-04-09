package ru.gilgamesh.abon.motot.ui.profile.enemyProfile

sealed class EnemyProfileCommand {
    data class LoadProfile(val contactId: Long?, val notificationId: Long?) : EnemyProfileCommand()
    data class LoadUserImg(val imgId: Long?, val sex: String?) : EnemyProfileCommand()
    data class LoadUserCoverImg(val imgId: Long?) : EnemyProfileCommand()
    data class LoadPhotoGallery(val contactId: Long) : EnemyProfileCommand()
    data class Subscribe(val contactId: Long?) : EnemyProfileCommand()
    data class Unsubscribe(val contactId: Long?) : EnemyProfileCommand()
    data class GetChat(val contactId: Long?) : EnemyProfileCommand()
}