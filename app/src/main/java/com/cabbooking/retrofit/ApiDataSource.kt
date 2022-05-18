package com.cabbooking.retrofit

import com.cabbooking.BuildConfig
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ApiDataSource @Inject constructor(
) {
    companion object {
        private const val BASE_URL = "http://apg.faastr.com/api/v1/"
    }

    fun create(): ServicesInterface {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getRetrofitClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(ServicesInterface::class.java)
    }

    private fun getRetrofitClient(
        authenticator: Authenticator? = null,
        connectTimeoutInSec: Long = 30,
        readTimeoutInSec: Long = 30,
        writeTimeoutInSec: Long = 30
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                chain.proceed(chain.request().newBuilder().also {
                    it.addHeader("Accept", "application/json")
                }.build())
            }.also { client ->
                authenticator?.let { client.authenticator(it) }
                if (BuildConfig.DEBUG) {
                    val logging = HttpLoggingInterceptor()
                    logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                    client.addInterceptor(logging)
                }
            }
            .connectTimeout(connectTimeoutInSec, TimeUnit.SECONDS)
            .readTimeout(readTimeoutInSec, TimeUnit.SECONDS)
            .writeTimeout(writeTimeoutInSec, TimeUnit.SECONDS)
            .build()
    }

//    fun create(
//        connectTimeoutInSec: Long = 30,
//        readTimeoutInSec: Long = 30,
//        writeTimeoutInSec: Long = 30
//    ): ServicesInterface {
//
//        val loggingInterceptor = HttpLoggingInterceptor()
//        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
//
//        val client = OkHttpClient.Builder()
//        client.addInterceptor(loggingInterceptor)
//        client.connectTimeout(connectTimeoutInSec, TimeUnit.SECONDS)
//        client.readTimeout(readTimeoutInSec, TimeUnit.SECONDS)
//        client.writeTimeout(writeTimeoutInSec, TimeUnit.SECONDS)
//
//        val retrofit = Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
//            .client(client.build())
//            .build()
//
//        return retrofit.create(ServicesInterface::class.java)
//    }

}