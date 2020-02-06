package com.varol.findmycar.data

import com.google.gson.JsonParseException
import com.squareup.moshi.JsonDataException
import com.varol.findmycar.internal.util.Failure
import com.varol.findmycar.internal.util.functional.Either
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Singleton class for handling API requests and responses
 */
object ServiceRequestHandler {

    /**
     * Handles all requests and responses with success, errors and exceptions,
     * fills data to Result data holder according to response type.
     * *** Only Rx Single type supported.
     */
    fun <T> sendRequest(call: Single<T>): Single<Either<Failure, T>> {
        return call
            .observeOn(AndroidSchedulers.mainThread())
            .map<Either<Failure, T>> {
                Either.Right(it)
            }
            .onErrorResumeNext { throwable: Throwable ->
                Single.just(
                    Either.Left(toFailure(throwable))
                )
            }
            .doOnError { throwable: Throwable -> Either.Left(toFailure(throwable)) }
    }
}

private fun toFailure(throwable: Throwable): Failure {
    return when (throwable) {
        is JsonParseException, is JsonDataException ->
            Failure.ParsingDataError
        else -> throwable as Failure
    }
}
