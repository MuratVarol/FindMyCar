package com.varol.findmycar.internal.view

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.tabs.TabLayout
import com.varol.findmycar.R
import com.varol.findmycar.internal.extension.setTabBackground

class RoundedCornerTabLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : TabLayout(context, attrs, defStyleAttr), TabLayout.OnTabSelectedListener {

    init {
        setBackgroundResource(R.drawable.background_cars_tab)
        addOnTabSelectedListener(this)
    }

    override fun onTabReselected(p0: Tab?) { }

    override fun onTabUnselected(p0: Tab?) {
        // no need to clear bg's
    }

    override fun onTabSelected(p0: Tab?) {
        updateTabBackground(p0)
    }

    private fun updateTabBackground(tab: Tab?) {
        tab?.let { setTabBackground(getTabBackground(it)) }
    }

    private fun getTabBackground(tab: Tab): Int {
        return when (tab.position) {
            0 -> R.drawable.selector_tab_color_left
            tabCount - 1 -> R.drawable.selector_tab_color_right
            else -> R.drawable.selector_tab_color_center
        }
    }
}
