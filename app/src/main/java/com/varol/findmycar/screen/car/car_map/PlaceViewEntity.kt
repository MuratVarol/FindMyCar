package com.varol.findmycar.screen.car.car_map

import com.google.android.gms.maps.model.LatLng

data class PlaceViewEntity(
    val tag : String,
    val position: LatLng,
    val title : String
)