package com.varol.findmycar.domain

import com.varol.findmycar.data.remote.repository.CarsRepository
import com.varol.findmycar.internal.util.Failure
import com.varol.findmycar.internal.util.functional.Either
import com.varol.findmycar.screen.car.CarViewEntity
import io.reactivex.Single

class GetAllCarsUseCase(
    private val repository: CarsRepository
) {
    fun getCars(): Single<Either<Failure, List<CarViewEntity>>> {
        return repository.getCars()
    }
}