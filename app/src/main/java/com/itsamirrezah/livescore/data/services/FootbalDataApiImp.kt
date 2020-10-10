package com.itsamirrezah.livescore.data.services

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FootbalDataApiImp() {

    companion object {

        fun getApi(): FootballDataApi {

            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            val okHttpClient = OkHttpClient()
                .newBuilder()
                .addInterceptor(FootballDataInterceptor())
                .addInterceptor(loggingInterceptor)
                .retryOnConnectionFailure(true)

            val retrofit = Retrofit.Builder()
                .baseUrl("http://api.football-data.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient.build())
                .build()

            return retrofit.create(FootballDataApi::class.java)
        }


    }

}