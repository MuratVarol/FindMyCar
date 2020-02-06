package com.varol.findmycar.screen.car

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.varol.findmycar.R
import com.varol.findmycar.screen.car.car_list.CarListFragment
import com.varol.findmycar.screen.car.car_map.CarsMapFragment
import java.security.InvalidParameterException

class CarsPagerAdapter(fm: FragmentManager, context: Context) :
        FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var tabs = emptyArray<TabItem>()

    companion object {
        const val LIST = 0
        const val MAP = 1
    }

    init {
        tabs += TabItem(context.resources.getString(R.string.list_pager), LIST)
        tabs += TabItem(context.resources.getString(R.string.map_pager), MAP)
    }

    override fun getCount(): Int = tabs.size

    override fun getItem(position: Int): Fragment {
        return when (tabs[position].index) {
            LIST -> CarListFragment()
            MAP -> CarsMapFragment()
            else -> throw InvalidParameterException("sure?")
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return tabs[position].title
    }
}

class TabItem(val title: String, val index: Int)
