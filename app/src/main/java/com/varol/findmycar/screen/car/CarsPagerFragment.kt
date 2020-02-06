package com.varol.findmycar.screen.car

import androidx.navigation.fragment.findNavController
import com.varol.findmycar.R
import com.varol.findmycar.base.BaseFragment
import com.varol.findmycar.databinding.FragmentCarListBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class CarsPagerFragment : BaseFragment<CarsViewModel, FragmentCarListBinding>(CarsViewModel::class) {
    override val getLayoutId: Int
        get() = R.layout.fragment_cars_pager

    override val viewModel: CarsViewModel by sharedViewModel(from = {
        findNavController().getViewModelStoreOwner(R.id.nav_graph_main)
    })



}