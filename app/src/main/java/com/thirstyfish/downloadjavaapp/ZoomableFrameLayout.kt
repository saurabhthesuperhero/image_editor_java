package com.thirstyfish.downloadjavaapp

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.FrameLayout
import java.lang.Double.max
import java.lang.Double.min
import kotlin.math.max
import kotlin.math.min

class ZoomableFrameLayout(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {
    private val scaleDetector: ScaleGestureDetector

    private var scaleFactor = 1f

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
            scaleFactor = max(0.1f, min(scaleFactor, 10f)).toFloat()
            scaleX = scaleFactor
            scaleY = scaleFactor
            return true
        }
    }
}
