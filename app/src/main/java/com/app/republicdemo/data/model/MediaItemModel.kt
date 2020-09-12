package com.app.republicdemo.data.model

import com.app.republicdemo.data.model.MediaItem
import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class MediaItemModel{

    @SerializedName("MediaItems")
    @Expose
    var mediaItems: List<MediaItem>? = null

}