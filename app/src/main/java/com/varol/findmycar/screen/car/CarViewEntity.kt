package com.varol.findmycar.screen.car

import android.graphics.Color
import com.google.android.gms.maps.model.LatLng
import com.varol.findmycar.data.remote.model.CleanlinessEnumEntity
import com.varol.findmycar.data.remote.model.FuelTypesEnumEntity
import com.varol.findmycar.data.remote.model.TransmissionEnumEntity
import com.varol.findmycar.internal.extension.EMPTY

data class CarViewEntity(
    val id: String = String.EMPTY,
    val modelName: String = String.EMPTY,
    val name: String = String.EMPTY,
    val make: String = String.EMPTY,
    val color: Int = Color.TRANSPARENT,
    val cleanliness: CleanlinessEnumEntity = CleanlinessEnumEntity.INVALID,
    val fuelType: FuelTypesEnumEntity = FuelTypesEnumEntity.INVALID,
    val transmission: TransmissionEnumEntity = TransmissionEnumEntity.INVALID,
    val fuelLevel: Int = 0,
    val plateNumber: String = String.EMPTY,
    val location: LatLng = LatLng(0.0, 0.0),
    val imageUrl: String = String.EMPTY

)
