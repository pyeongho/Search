package com.arch.search.data.keep.model.res

import com.google.gson.annotations.SerializedName

data class ResMeta(
    @SerializedName("status") val status: String?,
    @SerializedName("msg") val msg: String?,
    @SerializedName("response_id") val response_id: String?
    )

