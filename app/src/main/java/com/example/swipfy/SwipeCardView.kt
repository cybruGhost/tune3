package com.example.swipfy

import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.GestureDetectorCompat
import kotlin.math.abs

class SwipeCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var gestureDetector: GestureDetectorCompat
    private var onSwipeListener: OnSwipeListener? = null
    private var initialX: Float = 0f
    private var initialY: Float = 0f

    interface OnSwipeListener {
        fun onSwipeLeft()
        fun onSwipeRight()
        fun onSwipeTop()
        fun onSwipeBottom()
    }

    init {
        gestureDetector = GestureDetectorCompat(context, GestureListener())
    }

    fun setOnSwipeListener(listener: OnSwipeListener) {
        this.onSwipeListener = listener
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event)
    }

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            initialX = e.x
            initialY = e.y
            return true
        }

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            if (e1 == null) return false
            
            val diffX = e2.x - e1.x
            val diffY = e2.y - e1.y
            
            if (abs(diffX) > abs(diffY)) {
                if (abs(diffX) > SWIPE_THRESHOLD && abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        onSwipeListener?.onSwipeRight()
                    } else {
                        onSwipeListener?.onSwipeLeft()
                    }
                    return true
                }
            } else {
                if (abs(diffY) > SWIPE_THRESHOLD && abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeListener?.onSwipeBottom()
                    } else {
                        onSwipeListener?.onSwipeTop()
                    }
                    return true
                }
            }
            return false
        }
    }

    companion object {
        private const val SWIPE_THRESHOLD = 100
        private const val SWIPE_VELOCITY_THRESHOLD = 100
    }
}