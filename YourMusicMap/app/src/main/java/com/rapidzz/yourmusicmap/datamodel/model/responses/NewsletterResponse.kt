package com.rapidzz.mymusicmap.datamodel.model.responses

import java.io.Serializable

data class NewsletterResponse(
    val age: String,
    val city: String,
    val country: String,
    val email: String,
    val fanId: String,
    val gender: String,
    val id: String,
    val name: String,
    val umeId: String,
    val zip: String
) : Serializable