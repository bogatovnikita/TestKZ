package com.hedgehog.testkz

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import kotlin.math.roundToInt

class RainbowView @JvmOverloads constructor(
    context: Context,
    private val attrs: AttributeSet? = null,
    private val defStyleAttrs: Int = 0
) : View(context, attrs, defStyleAttrs) {

    private companion object {
        private const val START_ANGLE: Float = 180f
        private const val SWEEP_ANGLE: Float = 180f
    }

    private var rainbowStrokeWidth: Float = 20f
    private var rainbowColor: Int = android.R.color.holo_red_dark

    private val rainbowPaint: Paint = Paint().apply {
        isAntiAlias = true
        color = ContextCompat.getColor(context, rainbowColor)
        style = Paint.Style.STROKE
        strokeWidth = rainbowStrokeWidth
        strokeCap = Paint.Cap.ROUND
    }

    init {
        setupWithAttrs()
    }

    @SuppressLint("ResourceAsColor")
    private fun setupWithAttrs() {
        context.withStyledAttributes(attrs, R.styleable.RainbowView, defStyleAttrs) {
            rainbowStrokeWidth = getFloat(R.styleable.RainbowView_rainbow_stroke_width, 20f)
            rainbowColor = getColor(R.styleable.RainbowView_rainbow_color, 0)
        }
    }

    /**
     * Исключительно для отрисовки нашей View
     *
     * НЕ СОЗДАЕМ ОБЪЕКТОВ - иначе вьюшка будет медленно отрисовываться и фризить
     */
    override fun onDraw(canvas: Canvas?) {
        canvas ?: return
        canvas.drawArc(
            0f + rainbowStrokeWidth,
            0f + rainbowStrokeWidth,
            width.toFloat() - rainbowStrokeWidth,
            height.toFloat() - rainbowStrokeWidth,
            START_ANGLE,
            SWEEP_ANGLE,
            false,
            rainbowPaint
        )
    }

    /**
     * Здесь вычисляются размеры нашей вьюшки
     *
     * В функцию приходят два значения -
     *  1) widthSpec - ширина и мод
     *  2) heightSpec - высота и мод
     *  3) Мод - это ограничения вашей View, т.е. это требования родителя к размерам вашей вью,
     *  существует 3 возможных мода, 3 требования:
     *      EXACTLY, AT_MOST, UNSPECIFIED - более подробно про эти режимы
     *      (требования родителя к вашей View, можете почитать в инете)
     *  ), вместо частого прохода по этим режимам и вычислениям view в when-блоке, воспользуйтесь
     *  методом resolveSize()
     *
     * При вычислении размеров необходимо учитывать также те атрибуты, которые приходят нам для рисования
     * То есть, это может быть ширина рисуемой линии и тд и тп.
     *
     * Также, если ничего не приходит и значения находятся в созданных вами Paint'а,
     * их тоже учитываем и прибавляем к размеру.
     *
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = calculateWidth(widthMeasureSpec)
        val desiredHeight = calculateHeight(heightMeasureSpec)

        setMeasuredDimension(
            resolveSize(desiredWidth, widthMeasureSpec),
            resolveSize(desiredHeight, heightMeasureSpec)
        )
    }

    private fun calculateWidth(widthMeasureSpec: Int): Int {
        val size = MeasureSpec.getSize(widthMeasureSpec)
        return size + rainbowStrokeWidth.dpToPx() + paddingLeft + paddingRight
    }

    private fun calculateHeight(heightMeasureSpec: Int): Int {
        val size = MeasureSpec.getSize(heightMeasureSpec)
        return size + paddingTop + paddingBottom
    }

    private fun Float.dpToPx(): Int = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        Resources.getSystem().displayMetrics
    ).roundToInt()
}
