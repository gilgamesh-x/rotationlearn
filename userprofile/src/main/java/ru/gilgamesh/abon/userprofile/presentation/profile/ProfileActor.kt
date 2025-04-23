package ru.gilgamesh.abon.userprofile.presentation.profile

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import money.vivid.elmslie.core.store.Actor
import ru.gilgamesh.abon.userprofile.domain.repository.UserRepository

class ProfileActor(
    private val userRepository: UserRepository
) : Actor<ProfileCommand, ProfileEvent>() {
    override fun execute(command: ProfileCommand): Flow<ProfileEvent> = when (command) {
        is ProfileCommand.LoadProfile -> userRepository.getUserInfo()
            .mapEvents(eventMapper = { ProfileEvent.Internal.ProfileLoadingSuccess(it) }, errorMapper = {
                ProfileEvent.Internal.ProfileLoadingError(it)
            })

        is ProfileCommand.LoadUserCoverImg -> {
            flow {
                command.imgId?.let {
                    emit(ProfileEvent.Internal.LoadUserCoverImgGlide(it))
                } ?: run {
                    emit(ProfileEvent.Internal.LoadUserCoverImgRes)
                }
            }
        }

        is ProfileCommand.LoadUserImg -> {
            flow {
                command.imgId?.let { img ->
                    emit(ProfileEvent.Internal.LoadUserImgGlide(img))
                } ?: run {
                    command.sex?.let { sex ->
                        emit(ProfileEvent.Internal.LoadUserImgRes(sex))
                    } ?: run {
                        emit(ProfileEvent.Internal.LoadUserImgRes("M"))
                    }
                }
            }
        }

        is ProfileCommand.LoadPhotoGallery -> userRepository.getPhotoGalleryIds().mapEvents(
            eventMapper = { ProfileEvent.Internal.GalleryIdsLoadingSuccess(it) },
            errorMapper = { ProfileEvent.Internal.GalleryIdsLoadingError(it) })

        is ProfileCommand.DeletePhoto -> userRepository.deletePhoto(command.imgId).mapEvents(
            eventMapper = { ProfileEvent.Internal.GalleryIdsLoadingSuccess(it) },
            errorMapper = { ProfileEvent.Internal.GalleryDeleteError(it) })

        is ProfileCommand.AddPhoto -> userRepository.addPhoto(command.path).mapEvents(
            eventMapper = { ProfileEvent.Internal.AddPhotoSuccess(it) },
            errorMapper = { ProfileEvent.Internal.AddPhotoError(it) })
    }
}