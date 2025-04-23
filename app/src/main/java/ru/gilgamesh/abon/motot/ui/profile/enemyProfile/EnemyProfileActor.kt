package ru.gilgamesh.abon.motot.ui.profile.enemyProfile

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import money.vivid.elmslie.core.store.Actor

class EnemyProfileActor(
    private val enemyProfileRepository: EnemyProfileRepository
) : Actor<EnemyProfileCommand, EnemyProfileEvent>() {
    override fun execute(command: EnemyProfileCommand): Flow<EnemyProfileEvent> = when (command) {
        is EnemyProfileCommand.LoadProfile -> enemyProfileRepository.loadEnemyProfileInfo(
            contactId = command.contactId, notificationId = command.notificationId
        ).mapEvents(
            eventMapper = { EnemyProfileEvent.Internal.ProfileLoadingSuccess(it) },
            errorMapper = {
                EnemyProfileEvent.Internal.ProfileLoadingError(it)
            })

        is EnemyProfileCommand.LoadUserCoverImg -> {
            flow {
                command.imgId?.let {
                    emit(EnemyProfileEvent.Internal.LoadUserCoverImgGlide(it))
                } ?: run {
                    emit(EnemyProfileEvent.Internal.LoadUserCoverImgRes)
                }
            }
        }

        is EnemyProfileCommand.LoadUserImg -> {
            flow {
                command.imgId?.let { img ->
                    emit(EnemyProfileEvent.Internal.LoadUserImgGlide(img))
                } ?: run {
                    command.sex?.let { sex ->
                        emit(EnemyProfileEvent.Internal.LoadUserImgRes(sex))
                    } ?: run {
                        emit(EnemyProfileEvent.Internal.LoadUserImgRes("M"))
                    }
                }
            }
        }

        is EnemyProfileCommand.LoadPhotoGallery -> enemyProfileRepository.loadImageList(contactId = command.contactId)
            .mapEvents(
                eventMapper = { EnemyProfileEvent.Internal.GalleryIdsLoadingSuccess(it) },
                errorMapper = { EnemyProfileEvent.Internal.GalleryIdsLoadingError(it) })

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