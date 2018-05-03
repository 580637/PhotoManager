package com.brinjaul.androidlib.widgets


import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import com.brinjaul.androidlib.R
import com.brinjaul.androidlib.internal.utils.Utils


/*
 * Created by memfis on 7/6/16.
 * Updated by amadeu01 on 17/04/17
 */
class RecordButton @JvmOverloads constructor(context: Context , attrs: AttributeSet? = null , defStyleAttr: Int = 0) : android.support.v7.widget.AppCompatImageButton(context , attrs , defStyleAttr) {

    private val takePhotoDrawable: Drawable?
    private val startRecordDrawable: Drawable?
    private val stopRecordDrawable: Drawable?
    private val iconPadding = 8
    private val iconPaddingStop = 18

    private var listener: RecordButtonListener? = null

    interface RecordButtonListener {
        fun onRecordButtonClicked()
    }

    init {
        takePhotoDrawable = ContextCompat.getDrawable(context , R.drawable.take_photo_button)
        startRecordDrawable = ContextCompat.getDrawable(context , R.drawable.start_video_record_button)
        stopRecordDrawable = ContextCompat.getDrawable(context , R.drawable.stop_button_background)

        if (Build.VERSION.SDK_INT > 15)
            background = ContextCompat.getDrawable(context , R.drawable.circle_frame_background)
        else
            setBackgroundDrawable(ContextCompat.getDrawable(context , R.drawable.circle_frame_background))

        setOnClickListener(object : View.OnClickListener {
            private val CLICK_DELAY = 1000

            private var lastClickTime: Long = 0

            override fun onClick(view: View) {
                if (System.currentTimeMillis() - lastClickTime < CLICK_DELAY) {
                    return
                } else
                    lastClickTime = System.currentTimeMillis()

                if (listener != null) {
                    listener!!.onRecordButtonClicked()
                }
            }
        })
        isSoundEffectsEnabled = false
        setIconPadding(iconPadding)

        displayPhotoState()
    }

    //public void setup(@Configuration.MediaAction int mediaAction) {
    //    setMediaAction(mediaAction);
    //}

    private fun setIconPadding(paddingDP: Int) {
        val padding = Utils.convertDipToPixels(context , paddingDP)
        setPadding(padding , padding , padding , padding)
    }

    //public void setMediaAction(@Configuration.MediaAction int mediaAction) {
    //    this.mediaAction = mediaAction;
    //    if (listener != null) {
    //        if (Configuration.MEDIA_ACTION_PHOTO == mediaAction) {
    //            listener.setRecordState(Record.TAKE_PHOTO_STATE);
    //        } else {
    //            listener.setRecordState(Record.READY_FOR_RECORD_STATE);
    //        }
    //    }
    //}

    fun setRecordButtonListener(listener: RecordButtonListener) {
        this.listener = listener
    }

    fun displayVideoRecordStateReady() {
        setImageDrawable(startRecordDrawable)
        setIconPadding(iconPadding)
    }

    fun displayVideoRecordStateInProgress() {
        setImageDrawable(stopRecordDrawable)
        setIconPadding(iconPaddingStop)
    }

    fun displayPhotoState() {
        setImageDrawable(takePhotoDrawable)
        setIconPadding(iconPadding)
    }

}
