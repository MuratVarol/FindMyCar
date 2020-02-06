package com.varol.findmycar.internal.util

import android.content.Context
import com.varol.findmycar.internal.extension.networkInfo

class NetworkHandler(private val context: Context) {
    val isConnected: Boolean
        get() = context.networkInfo?.isConnected ?: false
}