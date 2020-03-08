package com.arch.search.data.repo.api


import com.arch.search.BuildConfig
import com.arch.search.constant.Constant
import com.arch.search.data.keep.ApiService
import com.arch.search.data.keep.model.req.ReqSearch
import com.arch.search.data.keep.model.res.ResResults
import io.reactivex.Single
import okhttp3.MediaType

import okhttp3.ResponseBody
import okio.BufferedSource
import retrofit2.Response


class ApiRemoteImpl(private val appService: ApiService) : ApiRemote {
    override fun getResults(search: ReqSearch): Single<Response<ResResults>> {
        return appService
            .getSearch(search.name, search.offset)
            .onErrorResumeNext(Single.just(Response.error(Constant.MAGIC_ERR_COMM, getEmptyBody())))
    }



    private fun getEmptyBody(): ResponseBody {
        return object : ResponseBody() {
            override fun contentType(): MediaType? {
                return null
            }

            override fun contentLength(): Long {
                return 0
            }

            override fun source(): BufferedSource? {
                return null
            }
        }
    }

}
