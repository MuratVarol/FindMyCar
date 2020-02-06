package com.varol.findmycar.data.remote.service

import com.varol.findmycar.data.remote.model.CarResponseModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.POST

interface Api {
    companion object {
        const val CARS_URL = "cars"
    }

    @GET(CARS_URL)
    fun getCars(
    ): Single<List<CarResponseModel>>

}
