package ru.gilgamesh.abon.motot.ui.sideNav.motoRating.distanceRating

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.gilgamesh.abon.motot.model.App
import javax.inject.Inject

@HiltViewModel
class RatingDistanceViewModel @Inject constructor(
    private val ratingDistanceRepository: RatingDistanceRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(RatingDistanceState())
    val uiState: StateFlow<RatingDistanceState> = _uiState.asStateFlow()

    private val _uiStateProfile = MutableStateFlow(RatingDistanceStateProfile())
    val uiStateProfile: StateFlow<RatingDistanceStateProfile> = _uiStateProfile.asStateFlow()

    /*init {
        loadProfile()
    }*/

    private fun getFirstPage() {
        if (_uiState.value.contentState is RatingDistanceLCEState.Loading) return
        viewModelScope.launch {
            _uiState.update {
                it.copy(contentState = RatingDistanceLCEState.Loading)
            }
            runCatching {
                if (_uiState.value.yearMode == 0) {
                    _uiState.update {
                        it.copy(
                            contentState = RatingDistanceLCEState.Content(ratingDistanceRepository.getRatingFirst())
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            contentState = RatingDistanceLCEState.Content(
                                ratingDistanceRepository.getRatingByYearFirst(
                                    _uiState.value.yearMode
                                )
                            )
                        )
                    }
                }
            }.onFailure {
                _uiState.update {
                    it.copy(contentState = RatingDistanceLCEState.Error(it.toString()))
                }

            }.onSuccess {}
        }
    }

    private fun getNextPage() {
        if (_uiState.value.contentState is RatingDistanceLCEState.Loading) return
        viewModelScope.launch {
            _uiState.update {
                it.copy(contentState = RatingDistanceLCEState.Loading)
            }
            runCatching {
                if (_uiState.value.yearMode == 0) {
                    _uiState.update {
                        it.copy(
                            contentState = RatingDistanceLCEState.Content(ratingDistanceRepository.getRatingNext())
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            contentState = RatingDistanceLCEState.Content(
                                ratingDistanceRepository.getRatingByYearNext(
                                    _uiState.value.yearMode
                                )
                            )
                        )
                    }
                }
            }.onFailure {
                _uiState.update {
                    it.copy(contentState = RatingDistanceLCEState.Error(it.toString()))
                }
            }.onSuccess {}
        }
    }

    fun handleIntent(intent: RatingDistanceIntent) {
        when (intent) {
            is RatingDistanceIntent.LoadFirstPageByYear -> {
                _uiState.value.yearMode = intent.year
                getFirstPage()
            }

            is RatingDistanceIntent.LoadFirstPageCurrentYear -> {
                _uiState.value.yearMode = 0
                getFirstPage()
            }

            is RatingDistanceIntent.LoadNextPage -> {
                getNextPage()
            }

            is RatingDistanceIntent.LoadProfile -> {
                loadProfile()
            }
        }
    }

    private fun loadProfile() {
        viewModelScope.launch {
            if (App.contactInfo != null) {
                _uiStateProfile.value.miniAvatarId = App.contactInfo.miniAvatarId
                _uiStateProfile.value.sex = App.contactInfo.sex
                _uiStateProfile.value.nickName = App.contactInfo.nickName ?: ""
                _uiStateProfile.value.motorcycle =
                    "${App.contactInfo.motoBrand ?: ""} ${App.contactInfo.motoModel ?: ""}"
                _uiStateProfile.value.distance = App.contactInfo.distance?.toInt() ?: 0
            }

            _uiStateProfile.update { it.copy(contentState = RatingDistanceLCEStateProfile.Content) }
        }
    }
}