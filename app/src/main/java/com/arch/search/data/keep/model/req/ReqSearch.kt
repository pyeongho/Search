package com.arch.search.data.keep.model.req

import com.google.gson.annotations.SerializedName

data class ReqSearch(
    @SerializedName("q") val name: String,
    @SerializedName("offset") val offset: String
)
