package com.varol.findmycar.screen.car.car_list

import androidx.navigation.fragment.findNavController
import com.varol.findmycar.R
import com.varol.findmycar.base.BaseFragment
import com.varol.findmycar.databinding.FragmentCarListBinding
import com.varol.findmycar.screen.car.CarsViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class CarListFragment : BaseFragment<CarsViewModel, FragmentCarListBinding>(CarsViewModel::class) {
    override val getLayoutId: Int
        get() = R.layout.fragment_car_list

    override val viewModel: CarsViewModel by sharedViewModel(from = {
        findNavController().getViewModelStoreOwner(R.id.nav_graph_main)
    })


}