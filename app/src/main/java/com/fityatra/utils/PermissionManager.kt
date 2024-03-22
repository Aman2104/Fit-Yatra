package com.fityatra.utils


import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class PermissionManager {
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var activity: AppCompatActivity

    private var isReadStoragePermissionGranted = false
    private var isPhysicalActivityPermissionGranted = false
    private var isReadMediaPermissionGranted = false
    private var isCameraPermissionGranted = false
    private var isPostNotificationPermissionGranted = false

    fun initPermissionLauncher(activity: AppCompatActivity) {
        this.activity = activity
        permissionLauncher =
            activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                permissions.entries.forEach { entry ->
                    when (entry.key) {
                        Manifest.permission.READ_EXTERNAL_STORAGE -> isReadStoragePermissionGranted =
                            entry.value

                        Manifest.permission.ACTIVITY_RECOGNITION -> isPhysicalActivityPermissionGranted =
                            entry.value

                        Manifest.permission.READ_MEDIA_IMAGES -> isReadMediaPermissionGranted =
                            entry.value

                        Manifest.permission.CAMERA -> isCameraPermissionGranted = entry.value
                        Manifest.permission.POST_NOTIFICATIONS -> isPostNotificationPermissionGranted =
                            entry.value
                    }
                }
            }
        requestPermission()
    }

    private fun requestPermission() {
        isReadStoragePermissionGranted = ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        isPhysicalActivityPermissionGranted = ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.ACTIVITY_RECOGNITION
        ) == PackageManager.PERMISSION_GRANTED
        isReadMediaPermissionGranted = ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.READ_MEDIA_IMAGES
        ) == PackageManager.PERMISSION_GRANTED
        isCameraPermissionGranted = ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        isPostNotificationPermissionGranted = ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED

        val permissionRequest: MutableList<String> = ArrayList()
        if (!isReadStoragePermissionGranted) {
            permissionRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (!isPhysicalActivityPermissionGranted) {
            permissionRequest.add(Manifest.permission.ACTIVITY_RECOGNITION)
        }
        if (!isReadMediaPermissionGranted) {
            permissionRequest.add(Manifest.permission.READ_MEDIA_IMAGES)
        }
        if (!isCameraPermissionGranted) {
            permissionRequest.add(Manifest.permission.CAMERA)
        }
        if (!isPostNotificationPermissionGranted) {
            permissionRequest.add(Manifest.permission.POST_NOTIFICATIONS)
        }

        if (permissionRequest.isNotEmpty()) {
            permissionLauncher.launch(permissionRequest.toTypedArray())
        }
    }


}
