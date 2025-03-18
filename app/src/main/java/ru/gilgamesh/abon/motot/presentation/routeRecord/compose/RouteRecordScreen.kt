@file:OptIn(ExperimentalMaterial3Api::class)

package ru.gilgamesh.abon.motot.presentation.routeRecord.compose

import android.location.Location
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yandex.runtime.Runtime.getApplicationContext
import kotlinx.coroutines.channels.consumeEach
import ru.gilgamesh.abon.motot.R
import ru.gilgamesh.abon.motot.presentation.routeRecord.RouteRecordViewModel
import ru.gilgamesh.abon.motot.presentation.routeRecord.RouteType
import ru.gilgamesh.abon.motot.services.MotoLocation
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

@Composable
fun RouteRecordScreen(
    startRecord: () -> Boolean, stopRecord: () -> Unit, onFinish: () -> Unit
) {

    val viewModel = hiltViewModel<RouteRecordViewModel>()
    val state by viewModel.state.collectAsState()
    val stateTimerActive by viewModel.stateTimer.collectAsState()
    val stateTimerTotal by viewModel.stateTimerTotal.collectAsState()
    val stateIsAutoPauseFlg by viewModel.stateIsAutoPause.collectAsState()
    val points = state.points.collectAsState()
    val timerActive = stateTimerActive.activeTime.collectAsState()
    val timerTotal = stateTimerTotal.totalTime.collectAsState()
    val isAutoPauseFlg = stateIsAutoPauseFlg.isAutoPause.collectAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.effect.consumeEach {
            if (it) {
                onFinish()
            }
        }
    }
    Column(
        Modifier
            .fillMaxSize()
            .padding(
                horizontal = 16.dp, vertical = 40.dp
            )
    ) {
        Content(
            modifier = Modifier.weight(1f),
            activeTime = timerActive.value,
            totalTime = timerTotal.value,
            isAutoPauseFlg = isAutoPauseFlg.value,
            points = points.value
        )

        MotoButton(modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
            style = if (timerTotal.value.value > Date(0)) {
                MotoButtonStyle.RedButton
            } else {
                MotoButtonStyle.OrangeButton
            },
            text = if (timerTotal.value.value > Date(0)) getApplicationContext().resources.getString(
                R.string.lbl_route_record_1
            ) else getApplicationContext().resources.getString(R.string.lbl_route_record_2),
            onClick = {
                if (timerTotal.value.value > Date(0)) {
                    viewModel.showFinalDialog()
                } else {
                    startRecord()
                }
            })

    }
    if (state.dialogShowed.value) {
        FinalDialog(onDiscard = {
            stopRecord()
            viewModel.savePoints(RouteType.DISTANCE)
        }, onPublish = {
            stopRecord()
            viewModel.savePoints(RouteType.PUBLIC)
        }, onSave = {
            stopRecord()
            viewModel.savePoints(RouteType.PRIVATE)
        }, onDismiss = {
            state.dialogShowed.value = false
        })
    }
}

@Composable
private fun Content(
    modifier: Modifier,
    activeTime: MutableState<Date>,
    totalTime: MutableState<Date>,
    isAutoPauseFlg: MutableState<Boolean>,
    points: List<MotoLocation> = emptyList()
) {
    var distance = 0f
    points.forEachIndexed { index, currentPoint ->
        if (points.lastIndex >= (index + 1)) {
            val nextPoint = points[index + 1]
            val results = floatArrayOf(1f)
            Location.distanceBetween(
                currentPoint.latitude,
                currentPoint.longitude,
                nextPoint.latitude,
                nextPoint.longitude,
                results
            )
            distance += results[0]
        }
    }

    val contentActiveTime = activeTime.value.time
    val contentTotalTime = totalTime.value.time
    val speed = if (points.isEmpty()) {
        0
    } else {
        points.sumOf { it.speed.toInt() } / points.count()
    }

    Column(
        modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = String.format(
                Locale.getDefault(), "%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(contentActiveTime) % 60,
                TimeUnit.MILLISECONDS.toMinutes(contentActiveTime) % 60,
                TimeUnit.MILLISECONDS.toSeconds(contentActiveTime) % 60,
            ), style = TextStyle(
                color = ValueColor,
                fontSize = 32.sp,
                lineHeight = 40.sp,
                fontFamily = FontFamily(fonts = listOf(Font(R.font.roboto)))
            )
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = getApplicationContext().resources.getString(R.string.lbl_route_record_3),
            style = TextStyle(
                color = DescriptionColor,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                fontFamily = FontFamily(fonts = listOf(Font(R.font.roboto)))
            )
        )
        Spacer(modifier = Modifier.height(18.dp))
        Text(
            text = String.format(
                Locale.getDefault(), "%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(contentTotalTime) % 60,
                TimeUnit.MILLISECONDS.toMinutes(contentTotalTime) % 60,
                TimeUnit.MILLISECONDS.toSeconds(contentTotalTime) % 60,
            ), style = TextStyle(
                color = ValueColor,
                fontSize = 24.sp,
                lineHeight = 32.sp,
                fontFamily = FontFamily(fonts = listOf(Font(R.font.roboto)))
            )
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = getApplicationContext().resources.getString(R.string.lbl_route_record_18),
            style = TextStyle(
                color = DescriptionColor,
                fontSize = 14.sp,
                lineHeight = 18.sp,
                fontFamily = FontFamily(fonts = listOf(Font(R.font.roboto)))
            )
        )

        Spacer(modifier = Modifier.height(18.dp))
        Spacer(modifier = Modifier.height(40.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = String.format(
                        getApplicationContext().resources.getString(R.string.lbl_route_record_10),
                        speed.toString()
                    ), style = TextStyle(
                        color = ValueColor,
                        fontSize = 24.sp,
                        lineHeight = 32.sp,
                        fontFamily = FontFamily(fonts = listOf(Font(R.font.roboto)))
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = getApplicationContext().resources.getString(R.string.lbl_route_record_4),
                    style = TextStyle(
                        color = DescriptionColor,
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        fontFamily = FontFamily(fonts = listOf(Font(R.font.roboto)))
                    )
                )
            }
            Column(
                modifier
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = String.format(Locale.getDefault(), "%.1f", distance / 1000),
                    style = TextStyle(
                        color = ValueColor,
                        fontSize = 24.sp,
                        lineHeight = 32.sp,
                        fontFamily = FontFamily(fonts = listOf(Font(R.font.roboto)))
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = getApplicationContext().resources.getString(R.string.lbl_route_record_5),
                    style = TextStyle(
                        color = DescriptionColor,
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        fontFamily = FontFamily(fonts = listOf(Font(R.font.roboto)))
                    )
                )
            }
        }
        if (isAutoPauseFlg.value) {
            Spacer(modifier = Modifier.height(28.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = getApplicationContext().resources.getString(R.string.lbl_route_record_19),
                        modifier = Modifier
                            .background(AutoPauseBackground)
                            .clip(shape = RoundedCornerShape(30.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                            .wrapContentWidth(),
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            fontFamily = FontFamily(fonts = listOf(Font(R.font.roboto)))
                        )
                    )
                }
            }
        }
//        Spacer(modifier = Modifier.height(24.dp))
//        val lazyListState = rememberLazyListState()
//        LazyColumn(
//            modifier = Modifier.fillMaxSize(),
//            verticalArrangement = Arrangement.spacedBy(12.dp),
//            state = lazyListState
//        ) {
//            points.forEach { point ->
//                item {
//                    PointView(
//                        modifier = modifier.fillMaxWidth(),
//                        latitude = point.latitude,
//                        longitude = point.longitude
//                    )
//                }
//            }
//        }
    }
}

@Composable
private fun PointView(modifier: Modifier, latitude: Double, longitude: Double) {
    Column(
        modifier
            .background(PointBackground, RoundedCornerShape(8.dp))
            .padding(vertical = 10.dp, horizontal = 20.dp)
    ) {
        Text(text = "Широта: $latitude; Долгота: $longitude.", color = Color.White)
    }
}

@Composable
private fun FinalDialog(
    onPublish: () -> Unit, onSave: () -> Unit, onDiscard: () -> Unit, onDismiss: () -> Unit
) {
    BasicAlertDialog(modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp)
        .background(PointBackground, RoundedCornerShape(12.dp))
        .padding(start = 10.dp, top = 28.dp, end = 10.dp, bottom = 14.dp),

        onDismissRequest = {
            onDismiss();
        },
        content = {
            Column {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = getApplicationContext().resources.getString(R.string.lbl_route_record_6),
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    style = TextStyle(
                        fontSize = 22.sp,
                        fontFamily = FontFamily(fonts = listOf(Font(R.font.roboto)))
                    )
                )
                Spacer(modifier = Modifier.height(28.dp))
                MotoButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    text = getApplicationContext().resources.getString(R.string.lbl_route_record_7)
                ) {
                    onPublish()
                }
                Spacer(modifier = Modifier.height(8.dp))
                MotoButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    text = getApplicationContext().resources.getString(R.string.lbl_route_record_8),
                    style = MotoButtonStyle.GreyButton
                ) {
                    onSave()
                }
                Spacer(modifier = Modifier.height(8.dp))
                MotoButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    text = getApplicationContext().resources.getString(R.string.lbl_route_record_9),
                    style = MotoButtonStyle.TextButton
                ) {
                    onDiscard()
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = getApplicationContext().resources.getString(R.string.lbl_route_record_17),
                    textAlign = TextAlign.Center,
                    color = DescriptionColor,
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = FontFamily(fonts = listOf(Font(R.font.roboto)))
                    )
                )
            }
        })
}

@Preview
@Composable
private fun FinalDialogPreview() {
    FinalDialog({}, {}, {}, {})
}

val DescriptionColor = Color(0xffcac5ca)
val ValueColor = Color(0xfffdf8fd)
val PointBackground = Color(0xFF363438)
val AutoPauseBackground = Color(0xFFFFA959)

@Preview
@Composable
private fun RouteRecordScreenPreview() {
    RouteRecordScreen(startRecord = { true }, stopRecord = {}, onFinish = {})
}

