package com.varol.findmycar.di

import android.content.Context
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.readystatesoftware.chuck.ChuckInterceptor
import com.serjltt.moshi.adapters.Wrapped
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.varol.findmycar.BuildConfig
import com.varol.findmycar.data.remote.service.Api
import com.varol.findmycar.internal.util.NetworkHandler
import com.varol.findmycar.internal.util.network.CacheInterceptor
import com.varol.findmycar.internal.util.network.ErrorHandlerInterceptor
import com.varol.findmycar.internal.util.network.MoshiConverters
import io.reactivex.schedulers.Schedulers
import okhttp3.Cache
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit


private const val CLIENT_TIME_OUT = 120L
private const val CLIENT_CACHE_SIZE = 10 * 1024 * 1024L
private const val CLIENT_CACHE_DIRECTORY = "http"


val networkModule = module {
    single { createCache(androidContext()) }
    single { createChuckInterceptor(androidContext()) }
    single { createCacheInterceptor() }
    single(name = "baseUrl") { getBaseUrl() }
    single { createOkHttpClient(get(), get(), get(), get()) }
    single { createMoshi() }
    single { createErrorHandlerInterceptor(get()) }
    single { createWebService<Api>(get(), get(), get(name = "baseUrl")) }

}


/**
 * Create Cache object for OkHttp instance
 */
fun createCache(context: Context): Cache = Cache(
    File(
        context.cacheDir,
        CLIENT_CACHE_DIRECTORY
    ),
    CLIENT_CACHE_SIZE
)


fun createMoshi(): Moshi {
    return Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .add(Wrapped.ADAPTER_FACTORY)
        .add(MoshiConverters())
        .build()
}

/**
 * returns ChuckInterceptor
 * ChuckInterceptor: API request-response interceptor for debugging API traffic
 */
fun createChuckInterceptor(context: Context): ChuckInterceptor {
    return ChuckInterceptor(context)
}

/**
 * returns CacheInterceptor
 */
fun createCacheInterceptor(): CacheInterceptor {
    return CacheInterceptor()
}

/**
 * returns ErrorHandlerInterceptor
 */
fun createErrorHandlerInterceptor(context: Context): ErrorHandlerInterceptor {
    return ErrorHandlerInterceptor(NetworkHandler(context))
}


/**
 * Create OkHttp client with required interceptors and defined timeouts
 */
fun createOkHttpClient(
    cache: Cache,
    chuckInterceptor: ChuckInterceptor,
    cacheInterceptor: CacheInterceptor,
    errorHandlerInterceptor: ErrorHandlerInterceptor

): OkHttpClient {
    val okHttpBuilder = OkHttpClient.Builder()
    okHttpBuilder
        .addInterceptor(chuckInterceptor)
        .addInterceptor(cacheInterceptor)
        .addInterceptor(errorHandlerInterceptor)
        .connectTimeout(CLIENT_TIME_OUT, TimeUnit.SECONDS)
        .writeTimeout(CLIENT_TIME_OUT, TimeUnit.SECONDS)
        .readTimeout(CLIENT_TIME_OUT, TimeUnit.SECONDS)
        .cache(cache)
    return okHttpBuilder.build()
}

/**
 * returns BASE_LINK for API
 */
fun getBaseUrl(): String {
    return BuildConfig.BASE_URL
}

/**
 * Create Retrofit Client
 *
 * <reified T> private func let us using reflection.
 * We can use generics and reflection so ->
 *  we don't have to define required Api Interface here
 */
inline fun <reified T> createWebService(
    okHttpClient: OkHttpClient,
    moshi: Moshi,
    baseUrl: String
): T {
    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
    return retrofit.create(T::class.java)
}
