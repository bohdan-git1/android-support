package com.rapidzz.yourmusicmap.other.util

import java.text.SimpleDateFormat
import java.util.*




class DateTimeUtils {

    companion object {
        val ISO_FORMAT = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val FORMAT_h_mm_a = SimpleDateFormat("h:mm a")
        val FORMAT_yyyy_MM_d = SimpleDateFormat("yyyy-MM-dd")
        val FORMAT_d_MMMM_yyyy = SimpleDateFormat("d MMMM yyyy")
        val FORMAT_dd_MMMM_yyyy = SimpleDateFormat("dd MMMM yyyy")
        val FORMAT_d_MMM_yyyy = SimpleDateFormat("d MMM yyyy")
        val FORMAT_d = SimpleDateFormat("d")
        val FORMAT_eeee = SimpleDateFormat("EEEE")
        val FORMAT_mmm = SimpleDateFormat("MMM")
        val FORMAT_mmm_dd = SimpleDateFormat("MMM dd")
        val FORMAT_HH_mm = SimpleDateFormat("HH:mm")


        fun toISODate(date: Date): String {
            ISO_FORMAT.timeZone = TimeZone.getTimeZone("GMT")
            return ISO_FORMAT.format(date)
        }

    }
}