package com.brinjaul.androidlib.internal.utils


import android.content.Context
import android.content.res.Configuration
import android.text.TextUtils
import android.util.TypedValue
import android.view.Surface
import android.view.WindowManager
import android.webkit.MimeTypeMap

object Utils {

    fun getDeviceDefaultOrientation(context: Context): Int {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val config = context.resources.configuration

        val rotation = windowManager.defaultDisplay.rotation

        return if ((rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) && config.orientation == Configuration.ORIENTATION_LANDSCAPE || (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) && config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Configuration.ORIENTATION_LANDSCAPE
        } else {
            Configuration.ORIENTATION_PORTRAIT
        }
    }

    fun getMimeType(url: String): String {
        var type: String = ""
        val extension = MimeTypeMap.getFileExtensionFromUrl(url)
        if (!TextUtils.isEmpty(extension)) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        } else {
            val reCheckExtension = MimeTypeMap.getFileExtensionFromUrl(url.replace("\\s+".toRegex() , ""))
            if (!TextUtils.isEmpty(reCheckExtension)) {
                type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(reCheckExtension)
            }
        }
        return type
    }

    fun convertDipToPixels(context: Context , dip: Int): Int {
        val resources = context.resources
        val px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP , dip.toFloat() , resources.displayMetrics)
        return px.toInt()
    }

}
