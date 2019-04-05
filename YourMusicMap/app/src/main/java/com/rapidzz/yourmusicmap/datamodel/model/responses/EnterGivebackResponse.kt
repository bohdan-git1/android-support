package com.rapidzz.mymusicmap.datamodel.model.responses

import java.io.Serializable

data class EnterGivebackResponse(
    val dateCreated: String,
    val eventId: String,
    val fanId: String,
    val givebackId: String,
    val id: String,
    val status: String
): Serializable