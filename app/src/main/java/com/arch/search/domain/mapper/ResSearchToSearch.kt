package com.arch.search.domain.mapper

import com.arch.search.constant.Constant
import com.arch.search.data.keep.model.res.ResMeta
import com.arch.search.data.keep.model.res.ResResults
import com.arch.search.entity.ApiErrorMessage
import com.arch.search.entity.Search
import com.arch.search.entity.Searchs
import com.arch.search.entity.ResultSearch
import com.arch.search.util.log
import okhttp3.Headers
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class ResSearchToSearch(response: Response<ResResults>) {
    var result: ResultSearch

    init {

        result = if (response.isSuccessful) {
            response.body()?.let { responseBody ->
                if(response.code() == 200){
                    processResSearch(responseBody, parseHeaderLink(response.headers()))
                }else{
                    ResultSearch.ApiError(
                        convertResErrorsToApiErrorMessage(
                            response.code(),
                            responseBody.meta
                        )
                    )
                }

            } ?: ResultSearch.Success(Searchs(0, HashMap(), ArrayList()))
        } else {
            if (response.code() == Constant.MAGIC_ERR_COMM) {
                ResultSearch.ConnectionError("connection error")
            } else {
                response.errorBody()?.string()?.let { error ->
                    try {

                        ResultSearch.ApiError(
                            convertResErrorsToApiErrorMessage(
                                response.code(),
                                ResMeta("","","")
                            )
                        )
                    } catch (e: Exception) {
                        log.e(e.message)
                        ResultSearch.CommonError(error)
                    }
                } ?: ResultSearch.CommonError(response.errorBody()?.string() ?: "unknown error")
            }
        }
    }

    private fun convertResErrorsToApiErrorMessage(
        responseCode: Int,
        resMeta: ResMeta?
    ): ApiErrorMessage {
        return ApiErrorMessage(
            responseCode,
            resMeta?.msg ?: "unknown error"
        )
    }

    private fun parseHeaderLink(headers: Headers): Map<String, String> {
        val link = headers.get("Link") ?: ""
        val map = HashMap<String, String>()
        try {
            if (link.isNotEmpty()) {
                val rels = link.split(",")
                for (rel in rels) {
                    val urls = rel.split(";")
                    if (urls.size == 2) {
                        val url = urls[0].replaceFirst("<", "").replace(">", "")
                        val more = urls[1].split("=")[1].replace("\"", "")
                        map[more] = url
                    }

                }
            }
        } catch (e: java.lang.Exception) {
        }
        return map
    }


    private fun processResSearch(resSearchs: ResResults, controlMap: Map<String, String>): ResultSearch {
        val itemCount = resSearchs.pagination?.total_count?.toInt()?:0
        val items = ArrayList<Search>()
        resSearchs.items?.let { lists ->
            for (item in lists) {
                items.add(
                    Search(
                        item.type ?: "",
                        item.id ?: "",
                        item.slug ?: "",
                        item.url ?: "",
                        item.images?.fixed_height_downsampled?.url ?: "",
                        item.bitly_url ?: "",
                        item.embed_url ?: "",
                        item.username ?: "",
                        item.source ?: "",
                        item.rating ?: "0",
                        item.source_post_url ?: "",
                        item.title ?: "",
                        searchEntity = null
                    )
                )
            }
        }
//        items.sort()
        return ResultSearch.Success(Searchs(itemCount, controlMap, items))
    }
}
