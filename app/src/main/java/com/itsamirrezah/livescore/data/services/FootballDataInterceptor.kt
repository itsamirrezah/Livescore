package com.itsamirrezah.livescore.data.services

import okhttp3.Interceptor
import okhttp3.Response

class FootballDataInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
            .newBuilder()
            .addHeader("X-Auth-Token", "71610fca6e1747489ffef41555f291e9")
            .build()

        return chain.proceed(request)
    }

}
