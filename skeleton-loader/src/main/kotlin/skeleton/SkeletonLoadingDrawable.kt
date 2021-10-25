package com.gauss.skeleton

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.Keep
import androidx.core.graphics.toRectF
import java.util.*


internal class SkeletonLoadingDrawable(
    val id: UUID,
    @ColorInt color: Int,
    private val radius: Int
) : Drawable() {

    private val paint: Paint = Paint()
    private var bounds: RectF? = null

    init {
        paint.color = color
    }

    override fun onBoundsChange(bounds: Rect?) {
        super.onBoundsChange(bounds)
        this.bounds = bounds?.toRectF()
    }

    override fun draw(canvas: Canvas) {
        bounds?.let { canvas.drawRoundRect(it, radius.toFloat(), radius.toFloat(), paint) }
    }

    @Keep
    fun setColor(@ColorInt color: Int) {
        paint.color = color
    }

    @Keep
    fun getColor() : Int {
        return paint.color
    }

    @Keep
    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    @Keep
    override fun getAlpha() : Int {
        return paint.alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    override fun getOpacity(): Int {
        return paint.alpha
    }

}
