package net.cornago.bullen.utils

import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

object WebUtils {
    private const val USER_AGENT =
        "bullen-mod"

    fun createClient(): OkHttpClient = OkHttpClient.Builder().apply {
        dispatcher(Dispatcher().apply {
            maxRequests = 10
            maxRequestsPerHost = 5
        })

        readTimeout(10, TimeUnit.SECONDS)
        connectTimeout(5, TimeUnit.SECONDS)
        writeTimeout(10, TimeUnit.SECONDS)

        addInterceptor { chain ->
            chain.request().newBuilder()
                .header("Accept", "application/json")
                .header("User-Agent", USER_AGENT)
                .build()
                .let { chain.proceed(it) }
        }
    }.build()
}