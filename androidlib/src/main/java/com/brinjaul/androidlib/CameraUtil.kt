package com.brinjaul.androidlib

import android.hardware.Camera
import android.util.Log

class CameraUtil {
    companion object {
        //静态方法用companion object {}包裹
        const val INVALID_CAMERA_ID: Int = -1

        fun findFrontFacingCamera(): Int {
            var cameraId = INVALID_CAMERA_ID
            // Search for the front facing camera
            var numberOfCameras = Camera.getNumberOfCameras()
            for (i in 0..numberOfCameras) {
                var info = Camera.CameraInfo()
                Camera.getCameraInfo(i , info)
                if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    Log.d("CameraUtil" , "Camera found")
                    cameraId = i
                    break
                }
            }
            return cameraId
        }

        fun findBackFacingCamera(): Int {
            var cameraId: Int = INVALID_CAMERA_ID
            var numberOfCameras: Int = Camera.getNumberOfCameras()
            for (i: Int in 0..numberOfCameras) {
                var info: Camera.CameraInfo = Camera.CameraInfo()
                Camera.getCameraInfo(i , info)
                if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    Log.d("CameraUtil" , "Camera found")
                    cameraId = i
                    break
                }
            }
            return cameraId
        }

        fun isCameraIdValid(cameraId: Int): Boolean {
            return cameraId != INVALID_CAMERA_ID
        }
    }
}