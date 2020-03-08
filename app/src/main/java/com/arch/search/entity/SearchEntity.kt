package com.arch.search.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.*

@Entity(tableName = "favorite_table")
data class SearchEntity(
    @PrimaryKey var slug: String = "",
    @ColumnInfo(name = "id") var id: String = "",
    @ColumnInfo(name = "title") var title: String = "",
    @ColumnInfo(name = "image_url") var image_url: String = "",
    @ColumnInfo(name = "url") var url: String = "",
    @ColumnInfo(name = "rating") var rating: String = ""
) : Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:""
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(slug)
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeString(image_url)
        parcel.writeString(url)
        parcel.writeString(rating)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SearchEntity> {
        override fun createFromParcel(parcel: Parcel): SearchEntity {
            return SearchEntity(parcel)
        }

        override fun newArray(size: Int): Array<SearchEntity?> {
            return arrayOfNulls(size)
        }
    }

}

