package com.arch.search.data.keep.model.res

import com.google.gson.annotations.SerializedName

data class ResItems(
    @SerializedName("type") val type: String?,
    @SerializedName("id") val id: String?,
    @SerializedName("slug") val slug: String?,
    @SerializedName("url") val url: String?,
    @SerializedName("bitly_url") val bitly_url: String?,
    @SerializedName("embed_url") val embed_url: String?,
    @SerializedName("username") val username: String?,
    @SerializedName("source") val source: String?,
    @SerializedName("rating") val rating: String?,
    @SerializedName("source_post_url") val source_post_url: String?,
    @SerializedName("images") val images: ResImage?,
    @SerializedName("title") val title: String?

)




