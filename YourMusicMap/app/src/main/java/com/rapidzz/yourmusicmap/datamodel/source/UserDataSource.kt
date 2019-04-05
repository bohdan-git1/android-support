package com.rapidzz.mymusicmap.datamodel.source

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

    interface GetUserInformationCallBack{
        fun onUserReceive(user: User)
        fun onPayloadError(error: ApiErrorResponse)
    }

    interface ForgotPasswordCallback {
        fun onEmailSent(response: ForgotPasswordResponse?)
        fun onPayloadError(error: ApiErrorResponse)
    }

    interface ResetVerficationEmailCallback {
        fun onEmailSent(response: NoContentResponse)
        fun onPayloadError(error: ApiErrorResponse)
    }

    interface UpdateProfileCallback{
        fun onProfileUpdated(user: User)
        fun onPayloadError(error: ApiErrorResponse)
    }

}