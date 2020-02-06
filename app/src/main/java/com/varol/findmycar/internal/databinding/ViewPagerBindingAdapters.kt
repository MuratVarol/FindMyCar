package com.varol.findmycar.internal.databinding

import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.varol.findmycar.internal.extension.getActivity
import com.varol.findmycar.screen.car.CarsPagerAdapter

object ViewPagerBindingAdapters {

    enum class PagerBindingEnum {
        CARS,
        OTHER
    }

    @JvmStatic
    @BindingAdapter("setupWithViewPager")
    fun setupWithViewPager(tabLayout: TabLayout, viewPager: ViewPager) {
        tabLayout.setupWithViewPager(viewPager)
    }

    @JvmStatic
    @BindingAdapter("pageMargin")
    fun setPageMargin(viewPager: ViewPager, margin: Float) {
        viewPager.pageMargin = margin.toInt()
    }

    @JvmStatic
    @BindingAdapter("pagerAdapter", "parentFragment", requireAll = false)
    fun setPagerAdapter(viewPager: ViewPager, type: PagerBindingEnum, fragment: Fragment?) {
        viewPager.offscreenPageLimit = 3
        val fm = fragment?.let {
            fragment.childFragmentManager
        } ?: run {
            (viewPager.context.getActivity() as FragmentActivity).supportFragmentManager
        }

        viewPager.adapter = when (type) {
            PagerBindingEnum.CARS -> CarsPagerAdapter(fm, viewPager.context)
            else -> null
        }
    }
}
