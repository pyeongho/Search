package com.arch.search.di

import com.arch.search.constant.Constant
import com.arch.search.data.keep.model.req.ReqSearch
import com.arch.search.data.keep.model.res.*
import com.arch.search.data.repo.api.ApiRemote
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.BufferedSource
import retrofit2.Response

class TestDummyApiRemoteImpl : ApiRemote {

    private fun getDummyList(name: String, i: Int) :ArrayList<ResItems>{
        val lists = ArrayList<ResItems>()
        for(index in 0 until i){
            lists.add(
                ResItems(
                    "${name}_$index",
                    "${name}_$index",
                    "${name}_$index",
                    "${name}_$index",
                    "${name}_$index",
                    "${name}_$index",
                    "${name}_$index",
                    "${name}_$index",
                    "${name}_$index",
                    "${name}_$index",
                    null,
                    "${name}_$index"
                )
            )
        }


        return lists
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

    override fun getResults(search: ReqSearch): Single<Response<ResResults>> {
        return when(search.name){
            "android" ->{
                Single.just(Response.success(ResResults(
                    getDummyList(search.name, 10),
                    null,
                    ResPagination("10","10","0")

                )))

            }
            "empty" ->{
                Single.just(Response.success(ResResults(
                    arrayListOf(),
                    null,
                    null
                )))
            }

            else ->{
                Single.just(Response.error(Constant.MAGIC_ERR_COMM, getEmptyBody()))
            }
        }
    }

}
