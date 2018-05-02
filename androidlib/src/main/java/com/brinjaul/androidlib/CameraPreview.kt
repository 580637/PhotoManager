package com.brinjaul.androidlib

import android.view.SurfaceView
import android.view.SurfaceHolder
import android.os.Build
import android.app.Activity
import android.hardware.Camera
import android.util.Log
import android.util.SparseIntArray
import android.view.Surface
import java.io.IOException


class CameraPreview(//持有Activity引用，为了获取屏幕方向，改成内部类会比较好
        private val mActivity: Activity) : SurfaceView(mActivity) , SurfaceHolder.Callback {

    private val mHolder: SurfaceHolder = holder
    private var mCamera: Camera? = null

    init {
        mHolder.addCallback(this)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            //API 11及以后废弃，需要时自动配置
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
        }
    }

    fun setCamera(camera: Camera?) {
        mCamera = camera
    }

    /**
     * 刷新相机
     */
    private fun refreshCamera() {
        if (mCamera != null) {
            requestLayout()
            //获取当前手机屏幕方向
            val rotation = mActivity.windowManager
                    .defaultDisplay.rotation
            //调整相机方向
            mCamera!!.setDisplayOrientation(
                    ORIENTATIONS.get(rotation))
            // 设置相机参数
            mCamera!!.setParameters(settingParameters())
        }
    }

    /**
     * 配置相机参数
     * @return
     */
    private fun settingParameters(): Camera.Parameters {
        // 获取相机参数
        val params = mCamera!!.getParameters()
        val focusModes = params.getSupportedFocusModes()
        //设置持续的对焦模式
        if (focusModes.contains(
                        Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)
        }

        //设置闪光灯自动开启
        if (focusModes.contains(Camera.Parameters.FLASH_MODE_AUTO)) {
            params.setFocusMode(Camera.Parameters.FLASH_MODE_AUTO)
        }

        return params
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            if (mCamera != null) {
                //surface创建，设置预览SurfaceHolder
                mCamera!!.setPreviewDisplay(holder)
                //开启预览
                mCamera!!.startPreview()
            }
        } catch (e: IOException) {
            Log.d(TAG , "Error setting camera preview: " + e.message)
        }

    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        // empty. Take care of releasing the Camera preview in your activity.
    }

    override fun surfaceChanged(holder: SurfaceHolder , format: Int , w: Int , h: Int) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.surface == null) {
            // preview surface does not exist
            return
        }

        // stop preview before making changes
        try {
            mCamera!!.stopPreview()
        } catch (e: Exception) {
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here
        refreshCamera()
        // start preview with new settings
        try {
            mCamera!!.setPreviewDisplay(mHolder)
            mCamera!!.startPreview()

        } catch (e: Exception) {
            Log.d(TAG , "Error starting camera preview: " + e.message)
        }

    }

    companion object {

        private val TAG = "TAG"

        /**
         * 控制相机方向
         */
        private val ORIENTATIONS = SparseIntArray()

        init {
            ORIENTATIONS.append(Surface.ROTATION_0 , 90)
            ORIENTATIONS.append(Surface.ROTATION_90 , 0)
            ORIENTATIONS.append(Surface.ROTATION_180 , 270)
            ORIENTATIONS.append(Surface.ROTATION_270 , 180)
        }
    }
}