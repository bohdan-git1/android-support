package com.rapidzz.mymusicmap.datamodel.model.fan
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class User : Serializable {
    @SerializedName("id")
    var id: String = ""
    @SerializedName("name")
    var name: String = ""
    @SerializedName("email")
    var email: String = ""
    @SerializedName("phone")
    var phone: String? = ""
    @SerializedName("image")
    var image: String? = ""
    @SerializedName("status")
    var status: String? = null


}
