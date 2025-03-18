package ru.gilgamesh.abon.motot.presentation.routeRecord

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.gilgamesh.abon.motot.domain.repositories.RouteRecordRepository
import ru.gilgamesh.abon.motot.services.MotoLocation
import java.util.Date
import java.util.Timer
import java.util.TimerTask
import javax.inject.Inject

@HiltViewModel
class RouteRecordViewModel @Inject constructor(
    private val routeRecordRepository: RouteRecordRepository
) : ViewModel() {

    val state = MutableStateFlow(RouteRecordState(points = routeRecordRepository.observe()))
    var stateTimer = MutableStateFlow(RouteRecordStateTimer(activeTime = routeRecordRepository.observeTimer()))
    var stateTimerTotal = MutableStateFlow(RouteRecordStateTotalTimer(totalTime = routeRecordRepository.observeTimerTotal()))
    val stateIsAutoPause = MutableStateFlow(RouteRecordStateIsAutoPauseTimer(isAutoPause = routeRecordRepository.observeIsAutoPause()))
    val effect = Channel<Boolean>()
    private var isFinish = false

    fun showFinalDialog() {
        state.value.dialogShowed.value = true
    }

    fun savePoints(routeType: RouteType) {
        if (!isFinish) {
            isFinish = true
            viewModelScope.launch {
                routeRecordRepository.savePoints(routeType)
                routeRecordRepository.clearPoints()
                routeRecordRepository.clearTimer()
                routeRecordRepository.clearTimerTotal()
                routeRecordRepository.clearIsAutoPause()
                effect.trySend(true)
            }
        }
    }
}

data class RouteRecordState(
    val speed: String? = null,
    val distance: String? = null,
    val points: StateFlow<List<MotoLocation>>,
    val dialogShowed: MutableState<Boolean> = mutableStateOf(false)
)

data class RouteRecordStateTimer(
    val activeTime: StateFlow<MutableState<Date>>
)

data class RouteRecordStateTotalTimer(
    val totalTime: StateFlow<MutableState<Date>>
)

data class RouteRecordStateIsAutoPauseTimer(
    val isAutoPause: StateFlow<MutableState<Boolean>>
)

enum class RouteType(val routeName: String) {
    PUBLIC("ROUTE"),
    PRIVATE("TRACKING"),
    DISTANCE("DISTANCE")
}