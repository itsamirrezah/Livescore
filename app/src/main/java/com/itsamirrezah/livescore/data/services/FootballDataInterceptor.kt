package com.itsamirrezah.livescore.data.services

import okhttp3.Interceptor
import okhttp3.Response

class FootballDataInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
            .newBuilder()
            .addHeader("X-Auth-Token", "24fb047edaa64dddb076659db5f5fd39")
            .build()

        return chain.proceed(request)
    }

}
