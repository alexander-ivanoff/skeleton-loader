package com.gauss.skeleton

import androidx.annotation.ColorInt

data class SkeletonConfiguration(
    @ColorInt val colorStart: Int,
    @ColorInt val colorEnd: Int,
    val cornerRadius: Int,
    val skeletonViewMarkerTag: String
) {

    companion object {
        fun default() = Builder().build()
    }

    class Builder {

        @ColorInt var colorStart: Int = 0
        @ColorInt var colorEnd: Int = 0
        var cornerRadius: Int = 0
        var skeletonViewMarkerTag : String = ""

        internal fun build()= SkeletonConfiguration(colorStart, colorEnd, cornerRadius, skeletonViewMarkerTag)

    }
}
