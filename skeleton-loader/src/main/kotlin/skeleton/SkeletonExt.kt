package com.gauss.skeleton

import android.view.View
import com.gauss.skeleton.manager.ObjectAnimatorsSkeletonManager
import com.gauss.skeleton.manager.SingleAnimatorSkeletonManager

private var skeletonManager : SkeletonManager? = SingleAnimatorSkeletonManager.default()

fun View.configureObjectAnimatorsSkeletonLoading(init: SkeletonConfiguration.Builder.() -> Unit) {
    val builder = SkeletonConfiguration.Builder()
    builder.init()
    skeletonManager?.reset()
    skeletonManager = ObjectAnimatorsSkeletonManager(builder.build())
}

fun View.configureSingleAnimatorSkeletonLoading(init: SkeletonConfiguration.Builder.() -> Unit) {
    val builder = SkeletonConfiguration.Builder()
    builder.init()
    skeletonManager?.reset()
    skeletonManager = SingleAnimatorSkeletonManager(builder.build())
}


fun View.showSkeletonLoading() {
    checkNotNull(skeletonManager).run { showSkeletonLoading(this@showSkeletonLoading) }

}

fun View.hideSkeletonLoading() {
    checkNotNull(skeletonManager).run { hideSkeletonLoading(this@hideSkeletonLoading) }
}

fun View.resetSkeletonLoading() {
    checkNotNull(skeletonManager).run {
        hideSkeletonLoading(this@resetSkeletonLoading)
        reset()
    }

}

