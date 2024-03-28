package com.fityatra.activities


import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.view.SurfaceView
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.fityatra.R


class CameraActivity : AppCompatActivity() {
    private var cameraManager: CameraManager? = null
    private var cameraDevice: CameraDevice? = null
    private var cameraCaptureSession: CameraCaptureSession? = null
    private var surfaceView: SurfaceView? = null
    private var currentCameraId: String? = null
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        surfaceView = findViewById<SurfaceView>(R.id.surfaceView)
        val switchCameraButton = findViewById<Button>(R.id.switchCameraButton)
        cameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
        if (cameraManager != null) {
            try {
                // Use the first available camera by default
                currentCameraId = cameraManager!!.cameraIdList[0]
                cameraManager!!.openCamera(currentCameraId!!, cameraStateCallback, null)
            } catch (e: CameraAccessException) {
                e.printStackTrace()
            }
        }
        switchCameraButton.setOnClickListener { switchCamera() }
    }

    private val cameraStateCallback: CameraDevice.StateCallback =
        object : CameraDevice.StateCallback() {
            override fun onOpened(camera: CameraDevice) {
                cameraDevice = camera
                createCameraPreview()
            }

            override fun onDisconnected(camera: CameraDevice) {
                cameraDevice!!.close()
            }

            override fun onError(camera: CameraDevice, error: Int) {
                cameraDevice!!.close()
                cameraDevice = null
            }
        }

    private fun createCameraPreview() {
        val surfaceHolder = surfaceView!!.holder
        surfaceHolder.setKeepScreenOn(true)
        try {
            val captureRequestBuilder =
                cameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            captureRequestBuilder.addTarget(surfaceHolder.surface)
            cameraDevice!!.createCaptureSession(
                listOf(surfaceHolder.surface),
                object : CameraCaptureSession.StateCallback() {
                    override fun onConfigured(session: CameraCaptureSession) {
                        if (cameraDevice == null) {
                            return
                        }
                        cameraCaptureSession = session
                        try {
                            cameraCaptureSession!!.setRepeatingRequest(
                                captureRequestBuilder.build(),
                                null, null
                            )
                        } catch (e: CameraAccessException) {
                            e.printStackTrace()
                        }
                    }

                    override fun onConfigureFailed(session: CameraCaptureSession) {
                        Toast.makeText(
                            this@CameraActivity,
                            "Failed to create camera session",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }, null
            )
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    @SuppressLint("MissingPermission")
    private fun switchCamera() {
        if (cameraDevice != null) {
            cameraDevice!!.close()
            cameraDevice = null
        }

        // Toggle between front and back cameras
        currentCameraId = if (currentCameraId == cameraManager!!.cameraIdList[0]) {
            cameraManager!!.cameraIdList[1]
        } else {
            cameraManager!!.cameraIdList[0]
        }
        try {
            cameraManager!!.openCamera(currentCameraId!!, cameraStateCallback, null)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    override fun onPause() {
        super.onPause()
        if (cameraCaptureSession != null) {
            cameraCaptureSession!!.close()
            cameraCaptureSession = null
        }
        if (cameraDevice != null) {
            cameraDevice!!.close()
            cameraDevice = null
        }
    }
}

