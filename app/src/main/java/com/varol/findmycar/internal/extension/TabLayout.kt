package com.varol.findmycar.internal.extension

import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.ViewCompat
import com.google.android.material.tabs.TabLayout

fun TabLayout.setTabBackground(resourceId: Int) {
    val tabStrip = getChildAt(0) as ViewGroup
    for (i in 0 until tabStrip.childCount) {
        val tabView = tabStrip.getChildAt(i)
        tabView?.let {
            val paddingStart = it.paddingStart
            val paddingTop = it.paddingTop
            val paddingEnd = it.paddingEnd
            val paddingBottom = it.paddingBottom
            ViewCompat.setBackground(it, AppCompatResources.getDrawable(it.context, resourceId))
            ViewCompat.setPaddingRelative(it, paddingStart, paddingTop, paddingEnd, paddingBottom)
        }
    }
}
