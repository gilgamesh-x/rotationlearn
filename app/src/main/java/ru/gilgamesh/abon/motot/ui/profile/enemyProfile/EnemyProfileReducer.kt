package ru.gilgamesh.abon.motot.ui.profile.enemyProfile

import money.vivid.elmslie.core.store.StateReducer
import ru.gilgamesh.abon.motot.payload.response.contact.UserAchievementResponse
import ru.gilgamesh.abon.motot.ui.bottomNav.chat.ChatCardActivity
import ru.gilgamesh.abon.motot.ui.profile.RecyclerViewImgGallery.ItemImg
import ru.gilgamesh.abon.motot.ui.profile.enemyProfile.EnemyProfileCommand.LoadPhotoGallery
import ru.gilgamesh.abon.motot.ui.profile.enemyProfile.EnemyProfileCommand.LoadProfile
import ru.gilgamesh.abon.motot.ui.profile.enemyProfile.EnemyProfileCommand.LoadUserCoverImg
import ru.gilgamesh.abon.motot.ui.profile.enemyProfile.EnemyProfileCommand.LoadUserImg
import ru.gilgamesh.abon.motot.ui.profile.enemyProfile.EnemyProfileEffect.LoadAvatarByGlide
import ru.gilgamesh.abon.motot.ui.profile.enemyProfile.EnemyProfileEffect.LoadAvatarBySex
import ru.gilgamesh.abon.motot.ui.profile.enemyProfile.EnemyProfileEffect.LoadCoverByGlide
import ru.gilgamesh.abon.motot.ui.profile.enemyProfile.EnemyProfileEffect.ShowProfile

class EnemyProfileReducer :
    StateReducer<EnemyProfileEvent, EnemyProfileState, EnemyProfileEffect, EnemyProfileCommand>() {
    override fun StateReducer<EnemyProfileEvent, EnemyProfileState, EnemyProfileEffect, EnemyProfileCommand>.Result.reduce(
        event: EnemyProfileEvent
    ) {
        when (event) {
            /**
             * Загружаем профиль
             */
            is EnemyProfileEvent.Ui.LoadProfile -> {
                state {
                    copy(isLoadingProfile = true)
                }
                commands {
                    +LoadProfile(
                        contactId = state.contactId, notificationId = state.notificationId
                    )
                }
            }

            /**
             * Загружаем аватарку
             */
            EnemyProfileEvent.Ui.LoadUserImg -> {
                commands {
                    +LoadUserImg(state.value?.miniAvatarId, state.value?.sex)
                }
            }

            /**
             * Загружаем фон
             */
            EnemyProfileEvent.Ui.LoadUserCoverImg -> {
                commands {
                    +LoadUserCoverImg(state.value?.coverId)
                }
            }

            /**
             * Загружаем галерею фотокарточек
             */
            EnemyProfileEvent.Ui.LoadPhotoGallery -> {
                commands {
                    +LoadPhotoGallery(state.value?.id!!)
                }
            }

            /**
             * Переход в галлерею фоток фулл сайз
             */
            is EnemyProfileEvent.Ui.GotoFullSizeGallery -> {
                effects {
                    +EnemyProfileEffect.GotoFullSizeGallery(
                        data = state.photoGalleryIds, position = event.position
                    )
                }
            }

            is EnemyProfileEvent.Ui.ClickAvatar -> {
                effects {
                    +EnemyProfileEffect.GotoFullSizeAvatar(
                        imgId = state.value?.avatarId
                    )
                }
            }

            is EnemyProfileEvent.Ui.GoBack -> {
                if (state.isChangedState) {
                    effects {
                        +EnemyProfileEffect.GotoBackRefresh
                    }
                } else {
                    effects {
                        +EnemyProfileEffect.GotoBack
                    }
                }
            }

            is EnemyProfileEvent.Ui.ClickShowMenu -> {
                effects {
                    +EnemyProfileEffect.ShowMenu
                }
            }

            is EnemyProfileEvent.Ui.ClickComplain -> {
                state.value?.id?.let {
                    effects {
                        +EnemyProfileEffect.ShowComplain(it)
                    }
                }
            }

            is EnemyProfileEvent.Ui.GotoAchievement -> {
                effects {
                    +EnemyProfileEffect.OpenAchievement(
                        state.value?.achievement ?: UserAchievementResponse()
                    )
                }
            }

            is EnemyProfileEvent.Ui.ClickSubscribe -> {
                commands {
                    +EnemyProfileCommand.Subscribe(state.value?.id)
                }
            }

            is EnemyProfileEvent.Ui.ClickUnsubscribe -> {
                commands {
                    +EnemyProfileCommand.Unsubscribe(state.value?.id)
                }
            }

            is EnemyProfileEvent.Ui.ClickMessage -> {
                if (state.prevActivity == ChatCardActivity.TAG) {
                    effects {
                        +EnemyProfileEffect.GotoBack
                    }
                } else {
                    commands {
                        +EnemyProfileCommand.GetChat(state.value?.id)
                    }
                }
            }

            is EnemyProfileEvent.Internal.ProfileLoadingError -> {
                state {
                    copy(isLoadingProfile = false)
                }
                effects { +EnemyProfileEffect.ShowError }
            }

            is EnemyProfileEvent.Internal.ProfileLoadingSuccess -> {
                var enableMsg: Boolean = state.enableMsg
                if (!enableMsg) {
                    enableMsg = event.value.subscription || event.value.dualLike
                }
                state {
                    copy(
                        isLoadingProfile = false,
                        value = event.value,
                        contactId = event.value.id,
                        enableMsg = enableMsg
                    )
                }
                effects { +ShowProfile(event.value, enableMsg) }
            }

            is EnemyProfileEvent.Internal.LoadUserCoverImgGlide -> {
                effects {
                    +LoadCoverByGlide(event.imgId)
                }
            }

            is EnemyProfileEvent.Internal.LoadUserCoverImgRes -> {
                effects {
                    +EnemyProfileEffect.LoadCoverByRes
                }
            }

            is EnemyProfileEvent.Internal.LoadUserImgGlide -> {
                effects {
                    +LoadAvatarByGlide(event.imgId)
                }
            }

            is EnemyProfileEvent.Internal.LoadUserImgRes -> {
                effects {
                    +LoadAvatarBySex(event.sex)
                }
            }

            is EnemyProfileEvent.Internal.GalleryIdsLoadingError -> {
                effects { +EnemyProfileEffect.ShowError }
            }

            is EnemyProfileEvent.Internal.GalleryIdsLoadingSuccess -> {
                state {
                    copy(photoGalleryIds = event.listIds)
                }
                effects {
                    +EnemyProfileEffect.LoadPhotoGallery(state.photoGalleryIds.stream().map { ItemImg(it.id) }.toList())
                }
            }

            is EnemyProfileEvent.Internal.SubscribeSuccess -> {
                state.isChangedState = true
                state.value?.let {
                    it.countSubscribers = it.countSubscribers + 1
                    effects {
                        +EnemyProfileEffect.Subscribed(it.countSubscribers)
                    }
                }
            }

            is EnemyProfileEvent.Internal.SubscribeError -> {
                effects {
                    +EnemyProfileEffect.ShowError
                }
            }

            is EnemyProfileEvent.Internal.UnsubscribeSuccess -> {
                state.isChangedState = true
                state.value?.let {
                    it.countSubscribers = it.countSubscribers - 1
                    effects {
                        +EnemyProfileEffect.Unsubscribed(it.countSubscribers)
                    }
                }
            }

            is EnemyProfileEvent.Internal.UnsubscribeError -> {
                effects {
                    +EnemyProfileEffect.ShowError
                }
            }

            is EnemyProfileEvent.Internal.GetChatSuccess -> {
                event.chat.id?.let {
                    effects {
                        +EnemyProfileEffect.GotoChat(
                            event.chat.id, state.value?.nickName ?: event.chat.name
                        )
                    }
                } ?: run {
                    effects {
                        +EnemyProfileEffect.GotoChatNew(
                            state.value?.id!!, state.value?.nickName ?: ""
                        )
                    }
                }
            }

            is EnemyProfileEvent.Internal.GetChatError -> {
                effects {
                    +EnemyProfileEffect.ShowError
                }
            }
        }
    }
}