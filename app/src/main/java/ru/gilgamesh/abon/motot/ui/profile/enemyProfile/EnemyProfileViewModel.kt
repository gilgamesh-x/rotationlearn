package ru.gilgamesh.abon.motot.ui.profile.enemyProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import money.vivid.elmslie.core.store.ElmStore
import ru.gilgamesh.abon.motot.ui.sideNav.motoRoadHelp.MotoRoadHelpEnemyCardActivity
import ru.gilgamesh.abon.motot.ui.sideNav.motoStudy.MotoStudyFragment
import ru.gilgamesh.abon.motot.ui.sideNav.motoTowTruck.TowTruckFragment
import javax.inject.Inject

@HiltViewModel
class EnemyProfileViewModel @Inject constructor(
    private val enemyProfileRepository: EnemyProfileRepository
) : ViewModel() {
    private val _state = MutableStateFlow(EnemyProfileState())
    val state: StateFlow<EnemyProfileState> = _state.asStateFlow()

    private val store = ElmStore(
        initialState = _state.value,
        reducer = EnemyProfileReducer(),
        actor = EnemyProfileActor(enemyProfileRepository)
    ).start()

    private val eventFlow = MutableSharedFlow<EnemyProfileEvent>()

    private val _effectsFlow = MutableSharedFlow<EnemyProfileEffect>()
    val effectsFlow: SharedFlow<EnemyProfileEffect> = _effectsFlow.asSharedFlow()

    init {
        observeEvents()
        observeEffects()
        observeState()
    }

    /**
     * Изменения состояния из редусера
     */
    private fun observeState() {
        viewModelScope.launch {
            store.states.collect { newState ->
                _state.value = newState
            }
        }
    }

    /**
     * Отслеживаем эффекты из редусера
     */
    private fun observeEffects() {
        viewModelScope.launch {
            store.effects.collect { effect ->
                _effectsFlow.emit(effect)
            }
        }
    }

    /**
     * Наполняем флоу для обработки событий
     */
    private fun observeEvents() {
        viewModelScope.launch {
            eventFlow.collect { event ->
                store.accept(event = event)
            }
        }
    }

    /**
     * События с UI
     */
    fun sendEvent(event: EnemyProfileEvent) {
        viewModelScope.launch {
            eventFlow.emit(event)
        }
    }

    fun putInitData(contactId: Long?, notificationId: Long?, prevActivity: String) {
        viewModelScope.launch {
            _state.value.contactId = contactId
            _state.value.notificationId = notificationId
            _state.value.prevActivity = prevActivity
            _state.value.enableMsg =
                prevActivity == TowTruckFragment.TAG || prevActivity == MotoStudyFragment.TAG || prevActivity == MotoRoadHelpEnemyCardActivity.TAG
        }
    }
}