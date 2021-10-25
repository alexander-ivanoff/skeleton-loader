package com.gauss.skeleton

import android.view.View

internal interface SkeletonManager {

    fun showSkeletonLoading(view: View)

    fun hideSkeletonLoading(view: View)

    fun reset()

}
