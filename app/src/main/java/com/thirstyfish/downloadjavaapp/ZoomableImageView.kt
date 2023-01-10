package com.thirstyfish.downloadjavaapp

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.ImageView
import java.lang.Double.max
import java.lang.Double.min
import kotlin.math.max
import kotlin.math.min

class ZoomableImageView(context: Context, attrs: AttributeSet) : androidx.appcompat.widget.AppCompatImageView(context, attrs) {
    private val scaleDetector: ScaleGestureDetector
    private var scaleFactor = 1f
    private val scaleListener = ScaleListener()
    init {
        scaleDetector = ScaleGestureDetector(context, ScaleListener())
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        scaleDetector.onTouchEvent(event)
        return true
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            scaleFactor *= detector.scaleFactor
            scaleFactor = max(0.1f, min(scaleFactor, 10f))
            scaleX = scaleFactor
            scaleY = scaleFactor
            return true
        }

        fun reset() {
            scaleFactor = 1f
            scaleX = 1f
            scaleY = 1f
        }
    }
    fun reset() {
        scaleListener.reset()
    }

}
