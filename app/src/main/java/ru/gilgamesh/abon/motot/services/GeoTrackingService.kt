package ru.gilgamesh.abon.motot.services

import android.Manifest
import android.app.Notification
import android.app.Notification.FLAG_ONGOING_EVENT
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.os.Looper
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.yandex.runtime.Runtime
import com.yandex.runtime.Runtime.getApplicationContext
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import ru.gilgamesh.abon.motot.R
import ru.gilgamesh.abon.motot.domain.repositories.RouteRecordRepository
import ru.gilgamesh.abon.motot.presentation.routeRecord.RouteRecordActivity
import java.util.Date
import java.util.Locale
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class GeoTrackingService : Service() {
    private var distance: Float = 0F
    private var isAdd = false
    private val fusedLocationClient by lazy { LocationServices.getFusedLocationProviderClient(this) }
    private val locationCallback by lazy {
        object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                for (location in locationResult.locations) {
                    MainScope().launch {
                        if (routeRecordRepository.getPointSize() > 0) {
                            val lastLocation = routeRecordRepository.getLastPoint()
                            if (lastLocation.latitude == location.latitude && lastLocation.longitude == location.longitude) {
                                isAdd = false
                            } else {
                                val results = floatArrayOf(1f)
                                Location.distanceBetween(
                                    location.latitude,
                                    location.longitude,
                                    lastLocation.latitude,
                                    lastLocation.longitude,
                                    results
                                )
                                distance += results[0]
                                isAdd = if (results[0] < 2) {
                                    false
                                } else {
                                    true
                                }
                            }
                        } else {
                            isAdd = true
                        }
                        if (isAdd) {
                            if (!activeTimerIsActive) {
                                resumeTimer()
                            }
                            routeRecordRepository.addPoint(
                                MotoLocation(
                                    location.latitude,
                                    location.longitude,
                                    location.speed * 3600 / 1000,
                                    System.currentTimeMillis()
                                )
                            )
                        } else {
                            pauseTimer()
                        }
                        routeRecordRepository.updateIsAutoPause(!isAdd)
                    }
                }
            }
        }
    }

    @Inject
    lateinit var routeRecordRepository: RouteRecordRepository
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        NotificationUtils.createNotification(this)
        MainScope().launch {
            routeRecordRepository.clearPoints()
            routeRecordRepository.clearTimer()
        }
        listenLocation()
        startTimerTotal()
        startTimer()
        return START_STICKY
    }

    private fun listenLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient.requestLocationUpdates(
            LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000L).build(),
            locationCallback,
            Looper.getMainLooper()
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        NotificationUtils.stopService(this)
        fusedLocationClient.removeLocationUpdates(locationCallback)
        timer.cancel()
        timerTotal.cancel()
    }

    private var timer = Timer()
    private val activeTime: MutableState<Date> = mutableStateOf(Date(0))
    private var activeTimerIsActive = false
    private fun startTimer() {
        activeTime.value = Date(0)
        timer.schedule(
            object : TimerTask() {
                override fun run() {
                    activeTime.value = Date(activeTime.value.time + 1000L)
                    MainScope().launch {
                        routeRecordRepository.updateTimer(activeTime)
                    }
                }

            }, activeTime.value, 1000L
        )
        activeTimerIsActive = true
    }

    private fun pauseTimer() {
        if (!activeTimerIsActive) return
        timer.cancel()
        activeTimerIsActive = false
    }

    private fun resumeTimer() {
        if (activeTimerIsActive) return
        timer = Timer()
        timer.schedule(
            object : TimerTask() {
                override fun run() {
                    activeTime.value = Date(activeTime.value.time + 1000L)
                    MainScope().launch {
                        routeRecordRepository.updateTimer(activeTime)
                    }
                }

            }, activeTime.value, 1000L
        )
        activeTimerIsActive = true
    }

    private val timerTotal = Timer()
    private val totalTime: MutableState<Date> = mutableStateOf(Date(0))
    private fun startTimerTotal() {
        totalTime.value = Date(0)
        timerTotal.schedule(
            object : TimerTask() {
                override fun run() {
                    totalTime.value = Date(totalTime.value.time + 1000L)
                    MainScope().launch {
                        routeRecordRepository.updateTimerTotal(totalTime)
                    }
                    val tm = String.format(
                        Locale.getDefault(),
                        "%02d:%02d:%02d",
                        TimeUnit.MILLISECONDS.toHours(totalTime.value.time) % 60,
                        TimeUnit.MILLISECONDS.toMinutes(totalTime.value.time) % 60,
                        TimeUnit.MILLISECONDS.toSeconds(totalTime.value.time) % 60
                    )
                    val dst = String.format(Locale.getDefault(), "%.1f", distance / 1000)
                    if (isAdd) {
                        NotificationUtils.showNotification(
                            String.format(
                                Runtime.getApplicationContext().resources.getString(
                                    R.string.dynamic_lbl_route_record_1
                                ), tm, dst
                            ), this@GeoTrackingService
                        )
                    } else {
                        NotificationUtils.showNotification(
                            String.format(
                                Runtime.getApplicationContext().resources.getString(
                                    R.string.dynamic_lbl_route_record_2
                                ), tm, dst
                            ), this@GeoTrackingService
                        )
                    }
                }

            }, totalTime.value, 1000L
        )
    }
}

object NotificationUtils {
    private const val CHANNEL_ID: String = "Geotracker"
    val NOTIFICATION_ID: Int = 1234

    fun createNotification(context: Service): Notification {
        val channelId = createChannel(context)
        val notification = buildNotification(context, channelId)
        context.startForeground(
            NOTIFICATION_ID, notification
        )
        return notification
    }

    fun stopService(context: Service) {
        context.stopForeground(NOTIFICATION_ID)
    }

    fun showNotification(text: String, ctx: Context) {
        val notification = buildNotification2(ctx, CHANNEL_ID, text)
        val notificationManager = ctx.getSystemService(NotificationManager::class.java)
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun buildNotification(
        context: Context, channelId: String
    ): Notification {
        // Create Pending Intents.
        val intent = Intent(context, RouteRecordActivity::class.java) // Укажите ваш экран
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        // Create a notification.
        val builder = Notification.Builder(context, channelId)

        builder.setContentTitle(getApplicationContext().resources.getString(R.string.lbl_route_record_11))
        builder.setContentText(getApplicationContext().resources.getString(R.string.lbl_route_record_12))
        builder.setSmallIcon(R.drawable.logo_app)
        builder.setStyle(Notification.BigTextStyle())
        builder.setContentIntent(pendingIntent)
        builder.setOngoing(false)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            builder.setFlag(FLAG_ONGOING_EVENT, false)
        }
        return builder.build()
    }

    private fun buildNotification2(
        context: Context, channelId: String, text: String
    ): Notification {
        // Create Pending Intents.
        val intent = Intent(context, RouteRecordActivity::class.java) // Укажите ваш экран

        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        // Create a notification.
        val builder = Notification.Builder(context, channelId)

        builder.setContentTitle(getApplicationContext().resources.getString(R.string.lbl_route_record_11))
        if (text.isEmpty()) {
            builder.setContentText(getApplicationContext().resources.getString(R.string.lbl_route_record_12))
        } else {
            builder.setContentText(text)
        }
        builder.setSmallIcon(R.drawable.logo_app)
        builder.setStyle(Notification.BigTextStyle())
        builder.setContentIntent(pendingIntent)
        builder.setOngoing(false)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            builder.setFlag(FLAG_ONGOING_EVENT, false)
        }
        return builder.build()
    }

    private fun createChannel(ctx: Service): String {
        // Create a channel.
        val notificationManager =
            ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelName: CharSequence = "Route tracking"
        //val importance = NotificationManager.IMPORTANCE_HIGH
        val importance = NotificationManager.IMPORTANCE_LOW
        val notificationChannel = NotificationChannel(
            CHANNEL_ID, channelName, importance
        )
        notificationManager.createNotificationChannel(
            notificationChannel
        )
        notificationChannel.setSound(null, null)  // Без звука
        notificationChannel.enableVibration(false) // Без вибрации
        return CHANNEL_ID
    }
}

data class MotoLocation(
    val latitude: Double, val longitude: Double, val speed: Float, val time: Long
)
