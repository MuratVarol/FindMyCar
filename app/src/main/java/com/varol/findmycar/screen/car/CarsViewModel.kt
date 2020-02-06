package com.varol.findmycar.screen.car

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.varol.findmycar.base.BaseAndroidViewModel
import com.varol.findmycar.domain.GetAllCarsUseCase
import com.varol.findmycar.internal.util.SingleLiveData

class CarsViewModel(
    context: Context,
    private val carsUseCase: GetAllCarsUseCase
) : BaseAndroidViewModel(context) {

    val carList = MutableLiveData<List<CarViewEntity>>()
    val isWaitingForLocation = SingleLiveData<Boolean>()
        .apply { value = false }

    init {
        fetchCars()
    }

    fun fetchCars(){
        showProgress()
        disposables.add(
            carsUseCase.getCars()
                .subscribe { carsList ->
                    hideProgress()
                    carsList.either(
                        ::handleFailure,
                        ::handleSuccessFetchOfCars
                    )
                }
        )
    }

    private fun handleSuccessFetchOfCars(carList: List<CarViewEntity>) {
        this.carList.postValue(carList)
    }

    fun onCarSelected(id: String) {
        //TODO
    }


    fun showLocationWaitingProgress() {
        showProgress()
        isWaitingForLocation.postValue(true)
    }

    fun hideLocationWaitingProgress() {
        hideProgress()
        isWaitingForLocation.postValue(false)
    }

}