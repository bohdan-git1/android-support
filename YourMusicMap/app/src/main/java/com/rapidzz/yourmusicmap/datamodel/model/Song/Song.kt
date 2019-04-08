package com.rapidzz.mymusicmap.datamodel.model.fan
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Song : Serializable {
    @SerializedName("id")
    var id: String = ""
    @SerializedName("title")
    var title: String = ""
    @SerializedName("path")
    var path: String? = ""
    @SerializedName("lat")
    var lat: String = ""
    @SerializedName("lng")
    var lng: String? = ""
}
