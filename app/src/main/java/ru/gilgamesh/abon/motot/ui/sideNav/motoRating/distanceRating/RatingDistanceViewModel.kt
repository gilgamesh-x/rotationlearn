package ru.gilgamesh.abon.motot.ui.sideNav.motoRating.distanceRating

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.gilgamesh.abon.motot.model.App
import ru.gilgamesh.abon.motot.payload.response.rating.PageableRatingItem
import ru.gilgamesh.abon.motot.ui.sideNav.motoRating.distanceRating.recycleViewRatingDistance.RatingItem
import javax.inject.Inject

@HiltViewModel
class RatingDistanceViewModel @Inject constructor(
    private val ratingDistanceRepository: RatingDistanceRepository
) : ViewModel() {
    private val intentFlow = MutableSharedFlow<RatingDistanceIntent>()

    private val _uiState = MutableStateFlow(RatingDistanceState())
    val uiState: StateFlow<RatingDistanceState> = _uiState.asStateFlow()

    private val _uiStateProfile = MutableStateFlow(RatingDistanceStateProfile())
    val uiStateProfile: StateFlow<RatingDistanceStateProfile> = _uiStateProfile.asStateFlow()

    init {
        viewModelScope.launch {
            intentFlow.toAction().execute(ratingDistanceRepository)
                .scan(_uiState.value.contentState, ::itemReducer).collect { newState ->
                    _uiState.value = RatingDistanceState(
                        contentState = newState,
                        yearMode = _uiState.value.yearMode,
                        isLoadingPage = _uiState.value.isLoadingPage,
                        currentPage = _uiState.value.currentPage
                    )
                }
        }
    }

    private fun Flow<RatingDistanceIntent>.toAction(): Flow<ItemAction> {
        return this.map { intent ->
            when (intent) {
                is RatingDistanceIntent.LoadFirstPageByYear -> {
                    _uiState.value.yearMode = intent.year
                    _uiState.value.currentPage = 0
                    ItemAction.LoadItems(_uiState.value.currentPage)
                }

                RatingDistanceIntent.LoadFirstPageCurrentYear -> {
                    _uiState.value.yearMode = 0
                    _uiState.value.currentPage = 0
                    ItemAction.LoadItems(_uiState.value.currentPage)
                }

                RatingDistanceIntent.LoadNextPage -> {
                    _uiState.value.currentPage++
                    ItemAction.LoadItems(_uiState.value.currentPage)
                }

                RatingDistanceIntent.LoadProfile -> ItemAction.LoadProfile
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun Flow<ItemAction>.execute(itemApiService: RatingDistanceRepository): Flow<ItemResult> {
        return this.flatMapConcat { action ->
            flow {
                when (action) {
                    is ItemAction.LoadItems -> {
                        emit(ItemResult.Loading(data = _uiState.value.contentState.data))
                        runCatching {
                            var response: PageableRatingItem? = if (_uiState.value.yearMode == 0) {
                                itemApiService.getDistanceRating(action.page)
                            } else {
                                itemApiService.getDistanceRatingByYear(
                                    action.page, _uiState.value.yearMode
                                )
                            }
                            emit(ItemResult.Success(response))
                        }.onFailure {
                            emit(
                                ItemResult.Failure(
                                    data = _uiState.value.contentState.data,
                                    throwable = it
                                )
                            )
                        }
                    }

                    ItemAction.LoadProfile -> Unit
                }
            }
        }
    }

    fun itemReducer(
        state: RatingDistanceLCEState<RatingItem>, result: ItemResult
    ): RatingDistanceLCEState<RatingItem> {
        return when (result) {
            is ItemResult.Loading -> {
                _uiState.value.isLoadingPage = true
                RatingDistanceLCEState.Loading(
                    data = state.data
                )
            }

            is ItemResult.Success -> {
                val currentData = if (result.data?.first ?: run {
                        true
                    }) {
                    emptyList()
                } else {
                    state.data
                }
                val newData = currentData + (result.data?.content ?: emptyList())
                _uiState.value.isLoadingPage = false
                RatingDistanceLCEState.Content(
                    data = newData
                )
            }

            is ItemResult.Failure -> {
                _uiState.value.isLoadingPage = false
                RatingDistanceLCEState.Error(
                    data = state.data, message = result.throwable.toString()
                )
            }
        }
    }

    fun handleIntent(intent: RatingDistanceIntent) {
        if (intent is RatingDistanceIntent.LoadProfile) {
            loadProfile()
        } else {
            if (_uiState.value.isLoadingPage) return
            viewModelScope.launch {
                intentFlow.emit(intent)
            }
        }
    }

    private fun loadProfile() {
        viewModelScope.launch {
            if (App.contactInfo != null) {
                with(_uiStateProfile.value) {
                    miniAvatarId = App.contactInfo.miniAvatarId
                    sex = App.contactInfo.sex
                    nickName = App.contactInfo.nickName ?: ""
                    motorcycle =
                        "${App.contactInfo.motoBrand ?: ""} ${App.contactInfo.motoModel ?: ""}"
                    distance = App.contactInfo.distance?.toInt() ?: 0
                }
            }

            _uiStateProfile.update { it.copy(contentState = RatingDistanceLCEStateProfile.Content) }
        }
    }
}