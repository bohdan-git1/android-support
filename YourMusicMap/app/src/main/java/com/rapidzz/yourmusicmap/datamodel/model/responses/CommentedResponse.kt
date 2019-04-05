package com.rapidzz.mymusicmap.datamodel.model.responses

data class CommentedResponse(
    val dateCreated: String,
    val fanId: String,
    val id: String,
    val inReplyTo: String,
    val message: String,
    val soRandomId: String,
    val status: String
)