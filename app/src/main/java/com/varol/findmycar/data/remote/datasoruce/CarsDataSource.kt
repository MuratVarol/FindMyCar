package com.varol.findmycar.data.remote.datasoruce

import com.varol.findmycar.data.ServiceRequestHandler
import com.varol.findmycar.data.remote.model.CarResponseModel
import com.varol.findmycar.data.remote.service.Api
import com.varol.findmycar.internal.util.Failure
import com.varol.findmycar.internal.util.functional.Either
import io.reactivex.Single

typealias service = ServiceRequestHandler

class CarsDataSource(private val api: Api) {

    fun getCars(
    ): Single<Either<Failure, List<CarResponseModel>>> {
        return service.sendRequest(
            api.getCars()
        )

    }

}
