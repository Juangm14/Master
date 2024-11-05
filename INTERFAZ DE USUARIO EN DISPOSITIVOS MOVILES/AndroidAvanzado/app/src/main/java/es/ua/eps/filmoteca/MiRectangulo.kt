package es.ua.eps.filmoteca

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class MiRectangulo @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint().apply { color = Color.RED }
    private var rectX = 100f
    private var rectY = 100f
    private val rectSize = 50f
    private var dentroDelRect = false

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRect(rectX, rectY, rectX + rectSize, rectY + rectSize, paint)
    }
    // ASÍ SERÍA LA PRIMERA PARTE DEL EJERCICIO 1
//    override fun onTouchEvent(event: MotionEvent): Boolean {
//        when (event.action) {
//            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
//                rectX = event.x - rectSize / 2
//                rectY = event.y - rectSize / 2
//                invalidate()
//                return true
//            }
//        }
//        return super.onTouchEvent(event)
//    }

    //úLTIMA PARTE DEL EJERCIO 1
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                dentroDelRect =
                    event.x in rectX..(rectX + rectSize) && event.y in rectY..(rectY + rectSize)
                if (dentroDelRect) {
                    rectX = event.x - rectSize / 2
                    rectY = event.y - rectSize / 2
                    invalidate()
                }
                return dentroDelRect
            }

            MotionEvent.ACTION_MOVE -> {
                if (dentroDelRect) {
                    rectX = event.x - rectSize / 2
                    rectY = event.y - rectSize / 2
                    invalidate()
                }
            }

            MotionEvent.ACTION_UP -> dentroDelRect = false
        }
        return true
    }
}