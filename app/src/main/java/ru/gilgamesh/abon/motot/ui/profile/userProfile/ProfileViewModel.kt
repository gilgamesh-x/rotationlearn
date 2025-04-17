package ru.gilgamesh.abon.motot.ui.profile.userProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import money.vivid.elmslie.core.store.ElmStore
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _state = MutableStateFlow(ProfileState())

    private val store = ElmStore(
        initialState = _state.value,
        reducer = ProfileReducer(),
        actor = ProfileActor(userRepository)
    ).start()


    private val eventFlow = MutableSharedFlow<ProfileEvent>()
    private val _effectsFlow = MutableSharedFlow<ProfileEffect>()
    val effectsFlow: SharedFlow<ProfileEffect> = _effectsFlow.asSharedFlow()


    init {
        observeEvents()
        observeEffects()
        observeState()
        observeRepositoryState()
    }

    /**
     * Следим за изменение
     */
    private fun observeRepositoryState() {
        //TODO("Not yet implemented")
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
    fun sendEvent(event: ProfileEvent) {
        viewModelScope.launch {
            eventFlow.emit(event)
        }
    }
}