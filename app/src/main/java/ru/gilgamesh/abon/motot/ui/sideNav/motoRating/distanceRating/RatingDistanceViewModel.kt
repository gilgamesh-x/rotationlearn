package ru.gilgamesh.abon.motot.ui.sideNav.motoRating.distanceRating

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RatingDistanceViewModel @Inject constructor(
    private val ratingDistanceRepository: RatingDistanceRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(RatingDistanceState())
    val uiState: StateFlow<RatingDistanceState> = _uiState.asStateFlow()

    private var yearMode: Int = 0
    private var isLoading: Boolean = false

    /*init {
        getFirstPage()
    }*/

    private fun getFirstPage() {
        if (isLoading) return
        isLoading = true
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true, items = emptyList(), error = "") }
            runCatching {
                if (yearMode == 0) {
                    _uiState.update {
                        it.copy(
                            items = ratingDistanceRepository.getRatingFirst(),
                            loading = false,
                            error = ""
                        )
                    }
                    isLoading = false
                } else {
                    _uiState.update {
                        it.copy(
                            items = ratingDistanceRepository.getRatingByYearFirst(yearMode),
                            loading = false,
                            error = ""
                        )
                    }
                    isLoading = false
                }
            }.onFailure {
                _uiState.update { it.copy(error = it.toString(), items = emptyList()) }
            }.onSuccess {
                _uiState.update { it.copy(loading = false, error = "", items = emptyList()) }
            }
        }
    }

    private fun getNextPage() {
        if (isLoading) return
        isLoading = true
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true, items = emptyList(), error = "") }
            runCatching {
                if (yearMode == 0) {
                    _uiState.update {
                        it.copy(
                            items = ratingDistanceRepository.getRatingNext(),
                            loading = false,
                            error = ""
                        )
                    }

                    isLoading = false
                } else {
                    _uiState.update {
                        it.copy(
                            items = ratingDistanceRepository.getRatingByYearNext(yearMode),
                            loading = false,
                            error = ""
                        )
                    }

                    isLoading = false
                }
            }.onFailure {
                _uiState.update { it.copy(error = it.toString(), items = emptyList()) }
            }.onSuccess {
                _uiState.update { it.copy(loading = false, error = "", items = emptyList()) }
            }
        }
    }

    fun handleIntent(intent: RatingDistanceIntent) {
        when (intent) {
            is RatingDistanceIntent.LoadFirstPageByYear -> {
                yearMode = intent.year
                getFirstPage()
            }

            is RatingDistanceIntent.LoadFirstPageCurrentYear -> {
                yearMode = 0
                getFirstPage()
            }

            is RatingDistanceIntent.LoadNextPage -> {
                getNextPage()
            }
        }
    }
}