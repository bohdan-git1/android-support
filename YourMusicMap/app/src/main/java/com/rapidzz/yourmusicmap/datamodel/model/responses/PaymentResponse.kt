package com.rapidzz.mymusicmap.datamodel.model.responses

data class PaymentResponse(
    val creditCardId: String,
    val id: String,
    val paidFor: String,
    val paidVia: String,
    val paidViaId: String,
    val parentId: String,
    val paymentAmount: Double,
    val paymentDate: String,
    val paymentId: String,
    val paymentReference: String
)