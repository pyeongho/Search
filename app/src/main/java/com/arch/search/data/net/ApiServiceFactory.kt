package com.arch.search.data.net


import com.arch.search.data.keep.ApiService
import com.google.gson.Gson

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object ApiServiceFactory {

    fun makeApiService(debug: Boolean, baseUrl: String): ApiService {
        val okHttpClient = makeOkHttpClient(makeLoggingInterceptor(debug))
        return makeApiService(baseUrl, okHttpClient, Gson())
    }

    private fun makeApiService(
        baseUrl: String,
        okHttpClient: OkHttpClient,
        gson: Gson
    ): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()


        return retrofit.create(ApiService::class.java)
    }

    private fun makeOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(httpLoggingInterceptor)
        return builder.build()
    }

    private fun makeLoggingInterceptor(debug: Boolean): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = if (debug)
            HttpLoggingInterceptor.Level.BODY
        else
            HttpLoggingInterceptor.Level.NONE
        return logging
    }
}
