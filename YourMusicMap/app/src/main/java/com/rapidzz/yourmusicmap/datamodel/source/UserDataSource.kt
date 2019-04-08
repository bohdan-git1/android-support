package com.rapidzz.mymusicmap.datamodel.source

import com.rapidzz.mymusicmap.datamodel.model.fan.Song
import com.rapidzz.mymusicmap.datamodel.model.fan.User
import com.rapidzz.mymusicmap.datamodel.model.responses.*


interface UserDataSource{
    interface RegisterCallback {
        fun onRegister(user: User)
        fun onPayloadError(error: ApiErrorResponse)
    }

    interface LoginCallback {
        fun onLogin(user: User)
        fun onPayloadError(error: ApiErrorResponse)
    }

    interface SaveSongCallback {
        fun onSaveSong(song: Song)
        fun onPayloadError(error: ApiErrorResponse)
    }

    interface PlaceCallback {
        fun onPlace(song: Song)
        fun onPayloadError(error: ApiErrorResponse)
    }

}