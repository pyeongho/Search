package com.arch.search.data.repo.api


import com.arch.search.data.keep.model.req.ReqSearch
import com.arch.search.data.keep.model.res.ResResults
import io.reactivex.Single
import retrofit2.Response

interface ApiRemote {

    fun getResults(search: ReqSearch): Single<Response<ResResults>>

}
