package ru.gilgamesh.abon.motot.presentation.routeRecord

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.util.Log
import android.widget.TextView
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker
import dagger.hilt.android.AndroidEntryPoint
import ru.gilgamesh.abon.motot.R
import ru.gilgamesh.abon.motot.presentation.routeRecord.compose.RouteRecordScreen
import ru.gilgamesh.abon.motot.services.GeoTrackingService

@AndroidEntryPoint
class RouteRecordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RouteRecordScreen(
                startRecord = {
                    if (checkPermissions()) {
                        startRecord()
                        true
                    } else {
                        false
                    }
                },
                stopRecord = {
                    stopService(
                        Intent(
                            this,
                            GeoTrackingService::class.java
                        )
                    )
                },
                onFinish = {
                    finish()
                })
        }
    }

    private fun checkPermissions(): Boolean {
        val permissionList = arrayListOf<Int>()
        permissionList.add(
            PermissionChecker.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )

        permissionList.add(
            PermissionChecker.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionList.add(
                PermissionChecker.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                )
            )
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            permissionList.add(
                PermissionChecker.checkSelfPermission(
                    this,
                    Manifest.permission.FOREGROUND_SERVICE_LOCATION
                )
            )
        }
//        permissionList.add(
//            PermissionChecker.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_BACKGROUND_LOCATION
//            )
//        )

        permissionList.add(
            PermissionChecker.checkSelfPermission(
                this,
                Manifest.permission.FOREGROUND_SERVICE
            )
        )
        if (permissionList.all {
                it == PermissionChecker.PERMISSION_GRANTED ||
                        it == PermissionChecker.PERMISSION_DENIED_APP_OP
            }
        ) {
            return true
        }
        noticeTrack(this)
        return false
    }
    // Permission result
    private val permissionRequest = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        val granted = permissions.entries.all {
            it.value == true
        }
        permissions.entries.forEach {
            Log.e("LOG_TAG", "${it.key} = ${it.value}")
        }

        if (granted) {
            startRecord()
        } else {
            // your code if permission denied
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d("TAG", "onRequestPermissionsResult: ")
        if (requestCode == REQ_PERMISSION_CODE &&
            grantResults.all {
                it == PermissionChecker.PERMISSION_GRANTED ||
                        it == PermissionChecker.PERMISSION_DENIED_APP_OP
            }
        ) {
            startRecord()
        }
    }


    private fun startRecord() {
        startForegroundService(
            Intent(
                this,
                GeoTrackingService::class.java
            )
        )
    }

    companion object {
        private const val REQ_PERMISSION_CODE = 1234
    }

    private fun requestNeedPermission() {
        permissionRequest.launch( arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.POST_NOTIFICATIONS,
            Manifest.permission.FOREGROUND_SERVICE_LOCATION,
            //Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            Manifest.permission.FOREGROUND_SERVICE
        ))
    }

    private fun noticeTrack(context: Context) {
        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setTitle(getString(R.string.lbl_route_record_16))
        alertDialog.setMessage(getString(R.string.lbl_permission2))
        alertDialog.setPositiveButton(android.R.string.ok) { _: DialogInterface?, _: Int ->
            requestNeedPermission()
        }
        val ald = alertDialog.create()
        ald.show()
        val msgTxt = ald.findViewById<TextView>(android.R.id.message);
        if (msgTxt != null) {
            msgTxt.movementMethod = LinkMovementMethod.getInstance()
        };
    }
}
