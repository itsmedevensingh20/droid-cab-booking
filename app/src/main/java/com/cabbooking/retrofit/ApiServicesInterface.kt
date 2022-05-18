package com.cabbooking.retrofit

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

interface ApiServicesInterface {

    companion object {
        fun create(
            baseUrl: String,
            connectTimeoutInSec: Long = 30,
            readTimeoutInSec: Long = 30,
            writeTimeoutInSec: Long = 30
        ): ServicesInterface {

            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder()
            client.addInterceptor(loggingInterceptor)
            client.connectTimeout(connectTimeoutInSec, TimeUnit.SECONDS)
            client.readTimeout(readTimeoutInSec, TimeUnit.SECONDS)
            client.writeTimeout(writeTimeoutInSec, TimeUnit.SECONDS)

            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
                .client(client.build())
                .build()

            return retrofit.create(ServicesInterface::class.java)
        }
    }
}
