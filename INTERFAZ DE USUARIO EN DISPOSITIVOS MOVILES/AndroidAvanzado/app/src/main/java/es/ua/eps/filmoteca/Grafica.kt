package es.ua.eps.filmoteca

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class Grafica @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paintRojo = Paint().apply { color = resources.getColor(R.color.custom_red) }
    private val paintAzul = Paint().apply { color = resources.getColor(R.color.custom_blue) }
    private var percentage : Float = 0f

    fun setPorcentaje(percentage: Float) {
        this.percentage = percentage
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = 100
        val height = 100
        setMeasuredDimension(width, height)
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val radio = (width.coerceAtMost(height) / 2).toFloat()
        canvas.drawCircle(radio, radio, radio, paintAzul)

        val angulo = (percentage / 100) * 360
        canvas.drawArc(0f, 0f, width.toFloat(), height.toFloat(), -90f, angulo, true, paintRojo)
    }

}