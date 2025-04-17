package ru.gilgamesh.abon.motot.ui.profile.userProfile

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import money.vivid.elmslie.core.store.Actor
import ru.gilgamesh.abon.motot.ui.profile.userProfile.ProfileEvent.Internal.GalleryDeleteError
import ru.gilgamesh.abon.motot.ui.profile.userProfile.ProfileEvent.Internal.GalleryIdsLoadingError
import ru.gilgamesh.abon.motot.ui.profile.userProfile.ProfileEvent.Internal.GalleryIdsLoadingSuccess
import ru.gilgamesh.abon.motot.ui.profile.userProfile.ProfileEvent.Internal.LoadUserCoverImgGlide
import ru.gilgamesh.abon.motot.ui.profile.userProfile.ProfileEvent.Internal.LoadUserImgGlide
import ru.gilgamesh.abon.motot.ui.profile.userProfile.ProfileEvent.Internal.LoadUserImgRes
import ru.gilgamesh.abon.motot.ui.profile.userProfile.ProfileEvent.Internal.ProfileLoadingError
import ru.gilgamesh.abon.motot.ui.profile.userProfile.ProfileEvent.Internal.ProfileLoadingSuccess

class ProfileActor(
    private val userRepository: UserRepository
) : Actor<ProfileCommand, ProfileEvent>() {
    override fun execute(command: ProfileCommand): Flow<ProfileEvent> = when (command) {
        is ProfileCommand.LoadProfile -> userRepository.getUserInfo()
            .mapEvents(eventMapper = { ProfileLoadingSuccess(it) }, errorMapper = {
                ProfileLoadingError(it)
            })

        is ProfileCommand.LoadUserCoverImg -> {
            flow {
                command.imgId?.let {
                    emit(LoadUserCoverImgGlide(it))
                } ?: run {
                    emit(ProfileEvent.Internal.LoadUserCoverImgRes)
                }
            }
        }

        is ProfileCommand.LoadUserImg -> {
            flow {
                command.imgId?.let { img ->
                    emit(LoadUserImgGlide(img))
                } ?: run {
                    command.sex?.let { sex ->
                        emit(LoadUserImgRes(sex))
                    } ?: run {
                        emit(LoadUserImgRes("M"))
                    }
                }
            }
        }

        is ProfileCommand.LoadPhotoGallery -> userRepository.getPhotoGalleryIds().mapEvents(
            eventMapper = { GalleryIdsLoadingSuccess(it) },
            errorMapper = { GalleryIdsLoadingError(it) })

        is ProfileCommand.DeletePhoto -> userRepository.deletePhoto(command.imgId).mapEvents(
            eventMapper = { GalleryIdsLoadingSuccess(it) },
            errorMapper = { GalleryDeleteError(it) })

        is ProfileCommand.AddPhoto -> userRepository.addPhoto(command.path).mapEvents(
            eventMapper = { ProfileEvent.Internal.AddPhotoSuccess(it) },
            errorMapper = { ProfileEvent.Internal.AddPhotoError(it) })
    }
}