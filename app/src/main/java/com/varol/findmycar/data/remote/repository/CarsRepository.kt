package com.varol.findmycar.data.remote.repository

import android.graphics.Color
import com.google.android.gms.maps.model.LatLng
import com.varol.findmycar.data.remote.datasoruce.CarsDataSource
import com.varol.findmycar.data.remote.model.*
import com.varol.findmycar.internal.extension.emptyIfNull
import com.varol.findmycar.internal.util.Failure
import com.varol.findmycar.internal.util.functional.Either
import com.varol.findmycar.screen.car.CarViewEntity
import io.reactivex.Single

class CarsRepository(
    private val carsDataSource: CarsDataSource
) {
    fun getCars(): Single<Either<Failure, List<CarViewEntity>>> {
        val result = carsDataSource.getCars()
        return result.map { cars ->
            cars.transform { it.mapToViewEntity() }
        }
    }

    private fun List<CarResponseModel>.mapToViewEntity(): List<CarViewEntity> {
        return map { carResponseModel ->
            with(carResponseModel) {
                CarViewEntity(
                    id.emptyIfNull(),
                    modelName.emptyIfNull(),
                    name.emptyIfNull(),
                    make.emptyIfNull(),
                    color.toColor(),
                    innerCleanliness ?: CleanlinessEnumEntity.INVALID,
                    fuelType ?: FuelTypesEnumEntity.INVALID,
                    transmission ?: TransmissionEnumEntity.INVALID,
                    (fuelLevel ?: 0.0).toInt() * 100,
                    licensePlate.emptyIfNull(),
                    LatLng(latitude ?: 0.0, longitude ?: 0.0),
                    carImageUrl.emptyIfNull()
                )
            }

        }
    }

    private fun ColorEnumEntity?.toColor(): Int {
        return when (this) {
            ColorEnumEntity.INVALID, ColorEnumEntity.BLACK -> Color.BLACK
            ColorEnumEntity.MIDNIGHT_BLACK -> hexToColorInt("#000316")
            ColorEnumEntity.MIDNIGHT_BLACK_METAL -> hexToColorInt("#232426")
            ColorEnumEntity.HOT_CHOCOLATE -> hexToColorInt("#aa8b74")
            ColorEnumEntity.LIGHT_WHITE -> hexToColorInt("#ffffff")
            ColorEnumEntity.ICED_CHOCALATE -> hexToColorInt("#b4a57a")
            ColorEnumEntity.ALPS_WHITE -> hexToColorInt("#a2a38b")
            ColorEnumEntity.SAPPHIRE_BLACK -> hexToColorInt("#08070d")
            ColorEnumEntity.ICED_CHOCOLATE_METAL -> hexToColorInt("#b4a57a")
            ColorEnumEntity.ABSOLUTE_BLACK_METAL -> hexToColorInt("#424c51")
            else -> Color.BLACK
        }
    }

    private fun hexToColorInt(hexColor: String): Int {
        return try {
            Color.parseColor(hexColor)
        } catch (ex: Exception) {
            Color.BLACK
        }
    }
}