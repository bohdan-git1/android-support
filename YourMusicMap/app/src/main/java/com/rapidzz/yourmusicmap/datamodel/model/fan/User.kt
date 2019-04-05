package com.rapidzz.mymusicmap.datamodel.model.fan
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class User : Serializable {
    @SerializedName("id")
    var id: String = ""
    @SerializedName("firstName")
    var firstName: String = ""
    @SerializedName("lastName")
    var lastName: String? = ""
    @SerializedName("email")
    var email: String = ""
    @SerializedName("phone")
    var phone: String? = ""
    @SerializedName("gender")
    var gender: String? = ""
    @SerializedName("age")
    var age: Int = 0
    @SerializedName("invisible")
    var invisible: Boolean = false
    @SerializedName("status")
    var status: String? = null


}
