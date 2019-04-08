package com.rapidzz.mymusicmap.datamodel.model.responses

import com.google.gson.annotations.SerializedName
import com.rapidzz.mymusicmap.datamodel.model.fan.Song
import com.rapidzz.mymusicmap.datamodel.model.fan.User
import java.io.Serializable


class SongListingResponse : Serializable {
        @SerializedName("status")
        var status: String = ""
        @SerializedName("message")
        val message: String = ""
        @SerializedName("error")
        val error: Boolean = false
        @SerializedName("data")
        val song: Song? = null
}
