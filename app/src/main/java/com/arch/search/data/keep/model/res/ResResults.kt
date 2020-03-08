package com.arch.search.data.keep.model.res

import com.arch.search.data.keep.model.res.ResItems
import com.google.gson.annotations.SerializedName


data class ResResults(
    @SerializedName("data") val items: List<ResItems>?,
    @SerializedName("meta") val meta: ResMeta?,
    @SerializedName("pagination") val pagination: ResPagination?



)