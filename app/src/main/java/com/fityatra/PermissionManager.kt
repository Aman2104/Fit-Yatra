package com.fityatra

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionManager {

    private const val ACTIVITY_RECOGNITION_PERMISSION_REQUEST = 1

    @RequiresApi(Build.VERSION_CODES.Q)
    fun requestActivityRecognitionPermission(activity: AppCompatActivity): Boolean {
        val activityRecognitionPermission = Manifest.permission.ACTIVITY_RECOGNITION
        val postNotificationPermission = Manifest.permission.POST_NOTIFICATIONS

        if (ContextCompat.checkSelfPermission(
                activity,
                activityRecognitionPermission
            ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                activity,
                postNotificationPermission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        } else {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(activityRecognitionPermission, postNotificationPermission),
                ACTIVITY_RECOGNITION_PERMISSION_REQUEST
            )
            return false
        }
    }
}
