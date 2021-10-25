package com.gauss.skeleton.manager

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import androidx.core.view.children
import com.gauss.skeleton.OriginalViewConfiguration
import com.gauss.skeleton.SkeletonConfiguration
import com.gauss.skeleton.SkeletonLoadingDrawable
import com.gauss.skeleton.SkeletonManager
import java.lang.ref.WeakReference
import java.util.*

internal class ObjectAnimatorsSkeletonManager(
    private val skeletonConfiguration: SkeletonConfiguration
): SkeletonManager {

    private val skeletonAnimators : MutableMap<UUID, Animator> = mutableMapOf()

    override fun showSkeletonLoading(view: View) {
        view.run {
            if (this is ViewGroup) {
                children.forEach { showSkeletonLoading(it)  }
            } else {
                if (tag != skeletonConfiguration.skeletonViewMarkerTag) return
                if (foreground is SkeletonLoadingDrawable) return

                val drawable = SkeletonLoadingDrawable(
                    UUID.randomUUID(),
                    skeletonConfiguration.colorStart,
                    skeletonConfiguration.cornerRadius
                )

                val animator = animator(drawable, this)
                skeletonAnimators[drawable.id] = animator

                alpha = 1f
                isEnabled = false
                this.foreground = drawable
                animator.start()
            }
        }
    }

    override fun hideSkeletonLoading(view: View) {
        view.run {
            if (this is ViewGroup) {
                children.forEach { hideSkeletonLoading(it) }
            } else {
                if (tag != skeletonConfiguration.skeletonViewMarkerTag) return

                (foreground as? SkeletonLoadingDrawable)?.let { skeletonDrawable ->
                    skeletonAnimators.remove(skeletonDrawable.id)?.cancel()
                }
                isEnabled = true
            }
        }
    }

    override fun reset() {
        skeletonAnimators.values.forEach { it.cancel() }
        skeletonAnimators.clear()
    }

    private fun animator(drawable: SkeletonLoadingDrawable, view: View) : Animator  {
        val viewConfiguration = OriginalViewConfiguration(
            alpha = view.alpha,
            isEnabled = view.isEnabled,
            foreground = view.foreground
        )
        val weakView = WeakReference(view)
        val updateListener = ValueAnimator.AnimatorUpdateListener { weakView.get()?.invalidate() }
        val endListener = endListener(weakView, viewConfiguration)

        return AnimatorSet().apply {
            playSequentially(
                ObjectAnimator.ofInt(drawable, "alpha", ALPHA_MIN_VALUE, ALPHA_MAX_VALUE).apply {
                    duration = ALPHA_DURATION_MS
                    addUpdateListener(updateListener)
                },
                ObjectAnimator.ofArgb(
                    drawable,
                    "color",
                    skeletonConfiguration.colorStart,
                    skeletonConfiguration.colorEnd
                ).apply {
                    duration = COLOR_CHANGE_DURATION_MS
                    repeatCount = ObjectAnimator.INFINITE
                    repeatMode = ObjectAnimator.REVERSE
                    addUpdateListener(updateListener)
                },
            )
            addListener(endListener)
        }
    }

    private fun endListener(
        weakView: WeakReference<View>,
        configuration: OriginalViewConfiguration
    ) : Animator.AnimatorListener {
        return object : Animator.AnimatorListener {

            override fun onAnimationStart(p0: Animator?) {

            }

            override fun onAnimationEnd(p0: Animator?) {
                configuration.let { viewConfiguration ->
                    weakView.get()?.run {
                        alpha = viewConfiguration.alpha
                        isEnabled = viewConfiguration.isEnabled
                        foreground = viewConfiguration.foreground
                    }
                }
            }

            override fun onAnimationCancel(p0: Animator?) {

            }

            override fun onAnimationRepeat(p0: Animator?) {

            }
        }
    }

    companion object {

        private const val ALPHA_MIN_VALUE = 0
        private const val ALPHA_MAX_VALUE = 255
        private const val ALPHA_DURATION_MS = 400L
        private const val COLOR_CHANGE_DURATION_MS = 800L

        fun default() = ObjectAnimatorsSkeletonManager(SkeletonConfiguration.default())
    }

}
