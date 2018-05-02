package com.brinjaul.manager

import android.content.pm.PackageManager
import android.hardware.Camera
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import com.brinjaul.androidlib.CameraUtil
import com.brinjaul.androidlib.CameraPreview
import kotlinx.android.synthetic.main.activity_camera.*
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream


class CameraActivity : AppCompatActivity() , Camera.PictureCallback {
    private var mCamera: Camera? = null
    private var mPreview: CameraPreview? = null
    private var mPermissions: Array<String> = arrayOf(android.Manifest.permission.CAMERA , android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun initCamera() {
        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // TODO 未检测到系统的相机
        } else {
            var cameraId = CameraUtil.findBackFacingCamera()
            if (!CameraUtil.isCameraIdValid(cameraId)) {
                // TODO 检测camera id 无效
            } else {
                if (safeCameraOpen(cameraId)) {
                    mCamera!!.startPreview();
                    if (mPreview != null) {
                        mPreview!!.setCamera(mCamera!!)
                    }
                } else {
                    // TODO 无法安全打开相机
                }
            }
        }
    }

    //打开相机的操作延迟到onResume()方法里面去执行，这样可以使得代码更容易重用，还能保持控制流程更为简单。当然也可以另起线程处理
    public override fun onResume() {
        super.onResume()
        initCamera()
    }

    private fun safeCameraOpen(id: Int): Boolean {
        var qOpened = false
        try {
            releaseCameraAndPreview()
            mCamera = Camera.open(id)
            qOpened = (mCamera != null)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return qOpened
    }

    /**
     * 重置相机
     */
    private fun resetCamera() {
        mCamera!!.startPreview()
        mPreview!!.setCamera(mCamera!!)
    }

    private fun startCamera() {

        mPreview = CameraPreview(this)
        camera_preview.addView(mPreview)
    }

    /**
     * 释放相机和预览
     */
    private fun releaseCameraAndPreview() {
        mPreview!!.setCamera(null)
        if (mCamera != null) {
            mCamera!!.release()
            mCamera = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var checkResult: Int = ContextCompat.checkSelfPermission(this , mPermissions[0])
        if (checkResult != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this , mPermissions , 100)
            return
        }
        setContentView(R.layout.activity_camera)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        startCamera()
        camera_take_photo.setOnClickListener {
            mCamera!!.takePicture(null , null , this)
        }
    }

    override fun onPictureTaken(data: ByteArray , camera: Camera) {
        val appDir = File(Environment.getExternalStorageDirectory() , Environment.DIRECTORY_DCIM + "/ByronTest")
        if (!appDir.exists()) {
            appDir.mkdirs()
        }
        val fileName = System.currentTimeMillis().toString() + ".jpg"
        val pictureFile = File(appDir , fileName)
        if (pictureFile.exists()) {
            pictureFile.delete()
        }
        pictureFile.createNewFile()
        var output: OutputStream? = null
        try {
            output = FileOutputStream(pictureFile)
            output.write(data)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (output != null) {
                try {
                    output.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int , permissions: Array<out String> , grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode , permissions , grantResults)

        when (requestCode) {
            100 -> {
                startCamera()
            }
        }
    }
}
