package com.rapidzz.mymusicmap.datamodel.model.responses

import com.google.gson.annotations.SerializedName
import com.rapidzz.mymusicmap.datamodel.model.fan.User
import java.io.Serializable


class UserResponse : Serializable {
        @SerializedName("status")
        var status: String = ""
        @SerializedName("message")
        val message: String = ""
        @SerializedName("error")
        val error: Boolean = false
        @SerializedName("data")
        val user: User? = null
}
