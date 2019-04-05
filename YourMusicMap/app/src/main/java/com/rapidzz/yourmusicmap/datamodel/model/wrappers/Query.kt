package com.rapidzz.mymusicmap.datamodel.model.wrappers

import com.rapidzz.yourmusicmap.other.util.DateTimeUtils
import java.util.*
import java.io.Serializable

/*
data class Query(
    val distance: Long,
    val lat: Double,
    val lng: Double,
    val date: String,
    val type: String
):Serializable
*/


class Query : Serializable {
    var distance: Long = 25000L
    var lat: Double = 0.0
    var lng: Double = 0.0
    var date: String = DateTimeUtils.toISODate(Date())
    var type: String = ""

    var isMusic: Boolean = true
    var isSport: Boolean = true
    var isEntertainment: Boolean = true
    var locationLabel = ""
    var isMyCurrentLocation = false

    constructor(){

    }

    /*constructor(distance: Long, lat: Double, lng: Double, date: String, type: String) {
        this.distance = distance
        this.lat = lat
        this.lng = lng
        this.date = date
        this.type = type
    }*/

    constructor(
        distance: Long,
        lat: Double,
        lng: Double,
        date: String,
        type: String,
        isMusic: Boolean,
        isSport: Boolean,
        isEntertainment: Boolean,
        locationLabel: String,
        isMyCurrentLocation: Boolean
        ) {
        this.distance = distance
        this.lat = lat
        this.lng = lng
        this.date = date
        this.type = type
        this.isMusic = isMusic
        this.isSport = isSport
        this.isEntertainment = isEntertainment
        this.locationLabel = locationLabel
        this.isMyCurrentLocation = isMyCurrentLocation
    }


    companion object {
        val COMING_UP = "coming-up"
        val HERE_AND_NOW = "here-and-now"
    }

}