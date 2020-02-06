package com.varol.findmycar.internal.extension

import android.location.Location
import com.google.android.gms.maps.model.LatLng

fun Location.toLatLng() = LatLng(this.latitude, this.longitude)