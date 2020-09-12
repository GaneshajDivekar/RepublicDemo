package com.app.republicdemo.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class MediaItem() :Parcelable {

    @SerializedName("Title")
    @Expose
    var title: String? = null

    @SerializedName("Image")
    @Expose
    var image: String? = null

    @SerializedName("Url")
    @Expose
    var url: String? = null

    @SerializedName("Duration")
    @Expose
    var duration: String? = null

    constructor(parcel: Parcel) : this() {
        title = parcel.readString()
        image = parcel.readString()
        url = parcel.readString()
        duration = parcel.readString()
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(title)
        dest?.writeString(image)
        dest?.writeString(url)
        dest?.writeString(duration)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MediaItem> {
        override fun createFromParcel(parcel: Parcel): MediaItem {
            return MediaItem(parcel)
        }

        override fun newArray(size: Int): Array<MediaItem?> {
            return arrayOfNulls(size)
        }
    }

}