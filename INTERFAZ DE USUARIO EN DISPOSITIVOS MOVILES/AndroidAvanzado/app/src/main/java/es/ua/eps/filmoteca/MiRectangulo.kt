package es.ua.eps.filmoteca

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat

class MiRectangulo @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), GestureDetector.OnGestureListener {

    private val paint = Paint().apply { color = Color.RED }
    private var rectX = 100f
    private var rectY = 100f
    private val rectSize = 50f
    private var dentroDelRect = false
    private var detectorGestos: GestureDetectorCompat
    private var colorActual = true // true para rojo, false para azul

    private var startX = 0f
    private var startY = 0f
    private var endX = 0f
    private var endY = 0f
    private var drawingVector = false

    init {
        detectorGestos = GestureDetectorCompat(context, this)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRect(rectX, rectY, rectX + rectSize, rectY + rectSize, paint)
        if (drawingVector) {
            val vectorPaint = Paint().apply { color =if (colorActual) Color.RED else Color.BLUE; strokeWidth = 5f }
            canvas.drawLine(startX, startY, endX, endY, vectorPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        detectorGestos.onTouchEvent(event)
        return true
    }

    override fun onDown(e: MotionEvent): Boolean {
        dentroDelRect = e.x in rectX..(rectX + rectSize) && e.y in rectY..(rectY + rectSize)
        return dentroDelRect
    }

    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        if (dentroDelRect) {
            rectX -= distanceX
            rectY -= distanceY
            invalidate()
        }
        return true
    }

    override fun onSingleTapUp(e: MotionEvent): Boolean {
        if (dentroDelRect) {
            colorActual = !colorActual
            paint.color = if (colorActual) Color.RED else Color.BLUE
            invalidate()
        }
        return true
    }

    override fun onLongPress(e: MotionEvent) {}

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        if (dentroDelRect) {
            if (e1 != null) {
                startX = e1.x
            }
            if (e1 != null) {
                startY = e1.y
            }
            endX = e2.x
            endY = e2.y
            rectX = e2.x - rectSize / 2
            rectY = e2.y - rectSize / 2
            drawingVector = true
            invalidate()
        }
        return true
    }

    override fun onShowPress(e: MotionEvent) {}
}
