package com.arch.search.data.keep


import com.arch.search.BuildConfig
import com.arch.search.data.keep.model.res.ResResults
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("gifs/search")
    fun getSearch(
        @Query("q") query: String,
        @Query("offset") offset: String,
        @Query("api_key") api_key: String= BuildConfig.API_KEY

    ): Single<Response<ResResults>>



}
