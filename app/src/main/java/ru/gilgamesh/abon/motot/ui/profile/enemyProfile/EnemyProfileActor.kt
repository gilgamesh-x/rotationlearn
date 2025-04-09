package ru.gilgamesh.abon.motot.ui.profile.enemyProfile

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import money.vivid.elmslie.core.store.Actor
import ru.gilgamesh.abon.motot.ui.profile.enemyProfile.EnemyProfileEvent.Internal.GalleryIdsLoadingError
import ru.gilgamesh.abon.motot.ui.profile.enemyProfile.EnemyProfileEvent.Internal.GalleryIdsLoadingSuccess
import ru.gilgamesh.abon.motot.ui.profile.enemyProfile.EnemyProfileEvent.Internal.LoadUserImgGlide
import ru.gilgamesh.abon.motot.ui.profile.enemyProfile.EnemyProfileEvent.Internal.LoadUserImgRes
import ru.gilgamesh.abon.motot.ui.profile.enemyProfile.EnemyProfileEvent.Internal.ProfileLoadingError
import ru.gilgamesh.abon.motot.ui.profile.enemyProfile.EnemyProfileEvent.Internal.ProfileLoadingSuccess

class EnemyProfileActor(
    private val enemyProfileRepository: EnemyProfileRepository
) : Actor<EnemyProfileCommand, EnemyProfileEvent>() {
    override fun execute(command: EnemyProfileCommand): Flow<EnemyProfileEvent> = when (command) {
        is EnemyProfileCommand.LoadProfile -> enemyProfileRepository.loadEnemyProfileInfo(
            contactId = command.contactId, notificationId = command.notificationId
        ).mapEvents(
            eventMapper = { ProfileLoadingSuccess(it) },
            errorMapper = {
                ProfileLoadingError(it)
            })

        is EnemyProfileCommand.LoadUserCoverImg -> {
            flow {
                command.imgId?.let {
                    emit(LoadUserImgGlide(it))
                } ?: run {
                    emit(EnemyProfileEvent.Internal.LoadUserCoverImgRes)
                }
            }
        }

        is EnemyProfileCommand.LoadUserImg -> {
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

        is EnemyProfileCommand.LoadPhotoGallery -> enemyProfileRepository.loadImageList(contactId = command.contactId)
            .mapEvents(
                eventMapper = { GalleryIdsLoadingSuccess(it) },
                errorMapper = { GalleryIdsLoadingError(it) })

        is EnemyProfileCommand.Subscribe -> enemyProfileRepository.invokeSubscribe(contactId = command.contactId)
            .mapEvents(
                eventMapper = { EnemyProfileEvent.Internal.SubscribeSuccess },
                errorMapper = { EnemyProfileEvent.Internal.SubscribeError(it) })

        is EnemyProfileCommand.Unsubscribe -> enemyProfileRepository.invokeUnsubscribe(contactId = command.contactId)
            .mapEvents(
                eventMapper = { EnemyProfileEvent.Internal.UnsubscribeSuccess },
                errorMapper = { EnemyProfileEvent.Internal.UnsubscribeError(it) })

        is EnemyProfileCommand.GetChat -> enemyProfileRepository.getChat(contactId = command.contactId)
            .mapEvents (
                eventMapper = { EnemyProfileEvent.Internal.GetChatSuccess(it) },
                errorMapper = { EnemyProfileEvent.Internal.GetChatError(it) }
            )
    }
}