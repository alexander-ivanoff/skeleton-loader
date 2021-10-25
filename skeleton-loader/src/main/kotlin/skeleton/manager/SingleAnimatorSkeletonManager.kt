package com.gauss.skeleton.manager

import android.animation.Animator
import android.animation.ValueAnimator
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import com.gauss.skeleton.OriginalViewConfiguration
import com.gauss.skeleton.R
import com.gauss.skeleton.SkeletonConfiguration
import com.gauss.skeleton.SkeletonLoadingDrawable
import com.gauss.skeleton.SkeletonManager
import java.util.*

internal class SingleAnimatorSkeletonManager(
    private val skeletonConfiguration: SkeletonConfiguration
) : SkeletonManager {

    private val skeletonAnimator = animator()

    private var skeletonViews: WeakHashMap<UUID, View> = WeakHashMap()

    private val viewConfigurations: MutableMap<UUID, OriginalViewConfiguration> = mutableMapOf()

    override fun showSkeletonLoading(view: View) {

        showSkeletonLoadingInt(view)

        when {
            skeletonAnimator.isPaused -> {
                skeletonAnimator.setupStartValues()
                skeletonAnimator.resume()
            }
            skeletonAnimator.isRunning && skeletonAnimator.isStarted -> {

            }
            else -> skeletonAnimator.start()
        }
    }

    override fun hideSkeletonLoading(view: View) {
        hideSkeletonLoadingInt(view)

        if (skeletonViews.isEmpty()) {
            skeletonAnimator.cancel()
        }
    }

    private fun showSkeletonLoadingInt(view: View) {
        view.run {
            if (this is ViewGroup) {
                children.forEach { showSkeletonLoadingInt(it) }
            } else {
                if (tag != skeletonConfiguration.skeletonViewMarkerTag) return

                if (skeletonViews.values.contains(this)) return

                val drawable = SkeletonLoadingDrawable(
                    UUID.randomUUID(),
                    skeletonConfiguration.colorStart,
                    skeletonConfiguration.cornerRadius
                )

                skeletonViews[drawable.id] = this

                viewConfigurations[drawable.id] = OriginalViewConfiguration(
                    alpha = alpha,
                    foreground = foreground,
                    isEnabled = isEnabled
                )

                foreground = drawable
                alpha = 1f
                isEnabled = false
            }
        }
    }

    private fun hideSkeletonLoadingInt(view: View) {
        view.run {
            if (this is ViewGroup) {
                children.forEach { hideSkeletonLoadingInt(it) }
            } else {
                (foreground as? SkeletonLoadingDrawable)?.let { skeletonDrawable ->
                    skeletonViews.remove(skeletonDrawable.id)
                    viewConfigurations.remove(skeletonDrawable.id)?.let { viewConfiguration ->
                        alpha = viewConfiguration.alpha
                        foreground = viewConfiguration.foreground
                        isEnabled = viewConfiguration.isEnabled
                    }
                }
            }
        }
    }

    override fun reset() {
        skeletonAnimator.cancel()
        skeletonViews.values.forEach { view ->
            (view.foreground as? SkeletonLoadingDrawable)?.let { skeletonDrawable ->
                viewConfigurations.remove(skeletonDrawable.id)?.run {
                    view.isEnabled = isEnabled
                    view.alpha = alpha
                    view.foreground = foreground
                }
            }
        }
        skeletonViews.clear()
        viewConfigurations.clear()
    }

    private fun animator(): Animator {
        return ValueAnimator.ofArgb(skeletonConfiguration.colorStart, skeletonConfiguration.colorEnd).apply {
            duration = COLOR_CHANGE_DURATION_MS
            repeatMode = ValueAnimator.REVERSE
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener { animator ->
                val color: Int = animator.animatedValue as Int
                skeletonViews.values.forEach { view ->
                    (view.foreground as? SkeletonLoadingDrawable)?.setColor(color)
                    view.invalidate()
                }
            }
        }
    }

    companion object {

        private const val COLOR_CHANGE_DURATION_MS = 800L

        fun default() = SingleAnimatorSkeletonManager(SkeletonConfiguration.default())
    }

}

