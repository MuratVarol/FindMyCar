package com.varol.findmycar.internal.util

import java.io.IOException

sealed class Failure : IOException() {
    class ServerError(var code: Int = 0, override var message: String) : Failure()
    class ApiError(var code: Int, override var message: String) : Failure()
    class UnknownError(override var message: String? = null) : Failure()
    class UnknownHostError : Failure()
    class HttpError(var code: Int, override var message: String) : Failure()
    class TimeOutError(override var message: String?) : Failure()
    object NoConnectivityError : Failure()
    object EmptyResponse : Failure()
    object ParsingDataError : Failure()
    object IgnorableError : Failure()
}