package ru.gilgamesh.abon.motot.ui.profile.userProfile

import money.vivid.elmslie.core.store.StateReducer
import ru.gilgamesh.abon.motot.data.models.UserInfoAchievement
import ru.gilgamesh.abon.motot.ui.profile.RecyclerViewImgGallery.ItemImg
import ru.gilgamesh.abon.motot.ui.profile.userProfile.ProfileCommand.LoadUserCoverImg
import ru.gilgamesh.abon.motot.ui.profile.userProfile.ProfileCommand.LoadUserImg
import ru.gilgamesh.abon.motot.ui.profile.userProfile.ProfileEffect.GotoFullAvatar
import ru.gilgamesh.abon.motot.ui.profile.userProfile.ProfileEffect.LoadAvatarByGlide
import ru.gilgamesh.abon.motot.ui.profile.userProfile.ProfileEffect.LoadAvatarBySex
import ru.gilgamesh.abon.motot.ui.profile.userProfile.ProfileEffect.LoadCoverByGlide
import ru.gilgamesh.abon.motot.ui.profile.userProfile.ProfileEffect.ShowProfile

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
                    copy(
                        isLoadingPhotoGallery = true, photoGalleryIds = event.listIds
                    )
                }
                effects {
                    +ProfileEffect.LoadPhotoGallery(event.listIds.stream().map { ItemImg(it.id) }
                        .toList())
                }
            }

            is ProfileEvent.Internal.ProfileLoadingError -> {
                effects {
                    +ProfileEffect.ShowError
                }
            }

            is ProfileEvent.Internal.ProfileLoadingSuccess -> {
                state {
                    copy(
                        isLoadingProfile = true, value = event.value
                    )
                }
                effects {
                    +ShowProfile(state.value!!)
                }
            }

            is ProfileEvent.Internal.LoadUserCoverImgGlide -> {
                effects {
                    +LoadCoverByGlide(event.imgId)
                }
            }

            ProfileEvent.Internal.LoadUserCoverImgRes -> {
                effects {
                    +ProfileEffect.LoadCoverByRes
                }
            }

            is ProfileEvent.Internal.LoadUserImgGlide -> {
                effects {
                    +LoadAvatarByGlide(event.imgId)
                }
            }

            is ProfileEvent.Internal.LoadUserImgRes -> {
                effects {
                    +LoadAvatarBySex(event.sex)
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
                    +LoadUserCoverImg(state.value?.coverId)
                }
            }

            ProfileEvent.Ui.LoadUserImg -> {
                commands {
                    +LoadUserImg(state.value?.miniAvatarId, state.value?.sex)
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
                        +GotoFullAvatar(it)
                    }
                }
            }

            ProfileEvent.Ui.ClickShowMenu -> {
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