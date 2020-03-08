package com.arch.search.data.keep.model.res

import com.google.gson.annotations.SerializedName

data class ResPagination(
    @SerializedName("total_count") val total_count: String?,
    @SerializedName("count") val count: String?,
    @SerializedName("offset") val offset: String?
    )

