package com.rapidzz.mymusicmap.datamodel.model.responses
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class AuthenticateResponse (
    @SerializedName("id")
    val id: String,
    @SerializedName("token")
    val token: String
): Serializable