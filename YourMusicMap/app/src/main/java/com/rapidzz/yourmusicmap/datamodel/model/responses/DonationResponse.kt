package com.rapidzz.mymusicmap.datamodel.model.responses

class DonationResponse {
    var __v: Int? = null
    var _id: String? = null
    var id: String? = null
    var amount: Double? = null
    var dateCreated: String? = null
    var fanId: String? = null
    var logs: List<Any>? = null
    var message: String? = null
    var payments: List<Any>? = null
    var recurring: Boolean = false
    var creditCardId: String? = null
    var status: String? = null
    var umeId: String? = null

    override fun toString(): String {
        return "DonationResponse(__v=$__v, _id=$_id, id=$id, amount=$amount, dateCreated=$dateCreated, fanId=$fanId, logs=$logs, message=$message, payments=$payments, recurring=$recurring, creditCardId=$creditCardId, status=$status, umeId=$umeId)"
    }


}