package ru.gilgamesh.abon.userprofile.presentation.profile

import money.vivid.elmslie.core.store.StateReducer
import ru.gilgamesh.abon.userprofile.data.model.UserInfoAchievement
import ru.gilgamesh.abon.userprofile.presentation.imageGallery.ItemImg
import java.util.stream.Collectors

class ProfileReducer : StateReducer<ProfileEvent, ProfileState, ProfileEffect, ProfileCommand>() {
    override fun StateReducer<ProfileEvent, ProfileState, ProfileEffect, ProfileCommand>.Result.reduce(
        event: ProfileEvent
    ) {
        when (event) {
            //ITERNAL
            is ProfileEvent.Internal.GalleryIdsLoadingError -> {
                effects {
                    +ProfileEffect.LoadPhotoGalleryError
                }
            }

            is ProfileEvent.Internal.GalleryIdsLoadingSuccess -> {
                state {
                    ProfileState(
                        isLoadingPhotoGallery = true, photoGalleryIds = event.listIds
                    )
                }
                effects {
                    +ProfileEffect.LoadPhotoGallery(event.listIds.stream().map { ItemImg(it.id) }
                        .collect(Collectors.toList()))
                }
            }

            is ProfileEvent.Internal.ProfileLoadingError -> {
                effects {
                    +ProfileEffect.ShowError
                }
            }

            is ProfileEvent.Internal.ProfileLoadingSuccess -> {
                state {
                    ProfileState(
                        isLoadingProfile = true, value = event.value
                    )
                }
                effects {
                    +ProfileEffect.ShowProfile(state.value!!)
                }

                commands {
                    +ProfileCommand.LoadUserCoverImg(state.value?.coverId)
                    +ProfileCommand.LoadUserImg(state.value?.miniAvatarId, state.value?.sex)
                }
            }

            is ProfileEvent.Internal.LoadUserCoverImgGlide -> {
                effects {
                    +ProfileEffect.LoadCoverByGlide(event.imgId)
                }
            }

            ProfileEvent.Internal.LoadUserCoverImgRes -> {
                effects {
                    +ProfileEffect.LoadCoverByRes
                }
            }

            is ProfileEvent.Internal.LoadUserImgGlide -> {
                effects {
                    +ProfileEffect.LoadAvatarByGlide(event.imgId)
                }
            }

            is ProfileEvent.Internal.LoadUserImgRes -> {
                effects {
                    +ProfileEffect.LoadAvatarBySex(event.sex)
                }
            }

            is ProfileEvent.Internal.GalleryDeleteError -> {
                effects {
                    +ProfileEffect.GalleryDeleteError
                }
            }

            is ProfileEvent.Internal.AddPhotoSuccess -> {
                commands {
                    +ProfileCommand.LoadPhotoGallery
                }
            }

            is ProfileEvent.Internal.AddPhotoError -> {
                effects {
                    +ProfileEffect.GalleryAddError
                }
            }

            //UI
            ProfileEvent.Ui.LoadPhotoGallery -> {
                commands {
                    +ProfileCommand.LoadPhotoGallery
                }
            }

            ProfileEvent.Ui.LoadProfile -> {
                commands {
                    +ProfileCommand.LoadProfile
                }
            }

            ProfileEvent.Ui.LoadUserCoverImg -> {
                commands {
                    +ProfileCommand.LoadUserCoverImg(state.value?.coverId)
                }
            }

            ProfileEvent.Ui.LoadUserImg -> {
                commands {
                    +ProfileCommand.LoadUserImg(state.value?.miniAvatarId, state.value?.sex)
                }
            }

            ProfileEvent.Ui.GotoListSubscribers -> {
                effects {
                    +ProfileEffect.GotoListSubscribers
                }
            }

            ProfileEvent.Ui.GotoListSubscriptions -> {
                effects {
                    +ProfileEffect.GotoListSubscriptions
                }
            }

            ProfileEvent.Ui.GotoAddFriend -> {
                effects {
                    +ProfileEffect.GotoAddFriend
                }
            }

            ProfileEvent.Ui.GotoEditProfile -> {
                effects {
                    +ProfileEffect.GotoEditProfile
                }
            }

            ProfileEvent.Ui.GotoAchievement -> {
                if (state.isLoadingProfile) {
                    effects {
                        +ProfileEffect.GotoAchievement(state.value?.achievement ?: UserInfoAchievement())
                    }
                }
            }

            ProfileEvent.Ui.GotoFullAvatar -> {
                state.value?.avatarId?.let {
                    effects {
                        +ProfileEffect.GotoFullAvatar(it)
                    }
                }
            }

            is ProfileEvent.Ui.ClickShowMenu -> {
                effects {
                    +ProfileEffect.ShowMenu
                }
            }

            ProfileEvent.Ui.ClickAddPhoto -> {
                effects {
                    +ProfileEffect.GotoPickPhoto
                }
            }

            is ProfileEvent.Ui.GotoFullSizeGallery -> {
                effects {
                    +ProfileEffect.GotoFullSizeGallery(state.photoGalleryIds, event.position)
                }
            }

            is ProfileEvent.Ui.DeletePhoto -> {
                commands {
                    +ProfileCommand.DeletePhoto(event.imgId)
                }
            }

            is ProfileEvent.Ui.AddPhoto -> {
                commands {
                    +ProfileCommand.AddPhoto(event.path)
                }
            }
        }
    }
}