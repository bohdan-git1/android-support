package com.rapidzz.mymusicmap.datamodel.source

import com.rapidzz.mymusicmap.datamodel.model.fan.Song
import com.rapidzz.mymusicmap.datamodel.model.fan.User
import com.rapidzz.mymusicmap.datamodel.model.responses.*


interface UserDataSource{

    interface IsTrackAvailableCallback{
        fun onResponse(trackAvailable: Boolean, message: String)
        fun onError(error: String)
    }

    interface RegisterCallback {
        fun onRegister(userResponse: UserResponse)
        fun onPayloadError(error: ApiErrorResponse)
    }

    interface LoginCallback {
        fun onLogin(user: User)
        fun onPayloadError(error: ApiErrorResponse)
    }

    interface SaveSongCallback {
        fun onSaveSong(response: SongResponse)
        fun onPayloadError(error: ApiErrorResponse)
    }

    interface GetSongCallback {
        fun onSongListing(response: SongListingResponse)
        fun onPayloadError(error: ApiErrorResponse)
    }

    interface PlaceCallback {
        fun onPlace(song: Song)
        fun onPayloadError(error: ApiErrorResponse)
    }

}