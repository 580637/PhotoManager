package com.brinjaul.androidlib.widgets

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.widget.AppCompatImageButton
import android.util.AttributeSet

import com.brinjaul.androidlib.R
import com.brinjaul.androidlib.internal.utils.Utils


/*
 * Created by memfis on 6/24/16.
 * Updated by amadeu01 on 17/04/17
 */
class CameraSwitchView @JvmOverloads
constructor(context: Context , attrs: AttributeSet? = null) : AppCompatImageButton(context , attrs) {

    private var frontCameraDrawable: Drawable? = null
    private var rearCameraDrawable: Drawable? = null
    private var padding = 5

    init {
        initializeView()
    }

    constructor(context: Context , attrs: AttributeSet , defStyleAttr: Int) : this(context , attrs) {}

    private fun initializeView() {
        val context = context
        frontCameraDrawable = ContextCompat.getDrawable(context , R.drawable.ic_camera_front_white_24dp)
        frontCameraDrawable = DrawableCompat.wrap(frontCameraDrawable!!)
        DrawableCompat.setTintList(frontCameraDrawable!!.mutate() , ContextCompat.getColorStateList(context , R.color.switch_camera_mode_selector))

        rearCameraDrawable = ContextCompat.getDrawable(context , R.drawable.ic_camera_rear_white_24dp)
        rearCameraDrawable = DrawableCompat.wrap(rearCameraDrawable!!)
        DrawableCompat.setTintList(rearCameraDrawable!!.mutate() , ContextCompat.getColorStateList(context , R.color.switch_camera_mode_selector))

        setBackgroundResource(R.drawable.circle_frame_background_dark)
        displayBackCamera()

        padding = Utils.convertDipToPixels(context , padding)
        setPadding(padding , padding , padding , padding)

        displayBackCamera()
    }

    fun displayFrontCamera() {
        setImageDrawable(frontCameraDrawable)
    }

    fun displayBackCamera() {
        setImageDrawable(rearCameraDrawable)
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        if (Build.VERSION.SDK_INT > 10) {
            if (enabled) {
                alpha = 1f
            } else {
                alpha = 0.5f
            }
        }
    }
}