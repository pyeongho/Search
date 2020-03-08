package com.arch.search.entity

import android.os.Parcel
import android.os.Parcelable

data class Search(
    val type: String,
    val id: String,
    val slug: String,
    val url: String,
    val image_url: String,
    val bitly_url: String,
    val embed_url: String,
    val username: String,
    val source: String,
    val rating: String,
    val source_post_url: String,
    val title: String,

    var searchEntity: SearchEntity? = null
) : Comparable<Search> ,Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readParcelable(SearchEntity::class.java.classLoader)
    ) {
    }

    override fun compareTo(other: Search): Int {
        return this.slug.compareTo(other.slug)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(type)
        parcel.writeString(id)
        parcel.writeString(slug)
        parcel.writeString(url)
        parcel.writeString(image_url)
        parcel.writeString(bitly_url)
        parcel.writeString(embed_url)
        parcel.writeString(username)
        parcel.writeString(source)
        parcel.writeString(rating)
        parcel.writeString(source_post_url)
        parcel.writeString(title)
        parcel.writeParcelable(searchEntity, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Search> {
        override fun createFromParcel(parcel: Parcel): Search {
            return Search(parcel)
        }

        override fun newArray(size: Int): Array<Search?> {
            return arrayOfNulls(size)
        }
    }
}
