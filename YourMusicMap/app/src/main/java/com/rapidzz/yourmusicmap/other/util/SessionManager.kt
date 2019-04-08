package com.rapidzz.mymusicmap.other.util

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.location.Address
import com.rapidzz.mymusicmap.datamodel.model.fan.User
import com.rapidzz.mymusicmap.datamodel.source.remote.RetrofitClientInstance
import com.rapidzz.mymusicmap.view.activities.LandingActivity
import org.intellij.lang.annotations.Language


class SessionManager {
    var context: Context? = null
    var pref: SharedPreferences


    constructor(context: Context) {
        this.context = context
        pref = context?.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }


    fun setUserLoggedIn(islogin: Boolean){
        with(pref.edit()) {
            putBoolean(IS_USER_LOGGED_IN, islogin)
            commit()
        }
    }

    fun isLoggedIn(): Boolean {
        return pref.getBoolean(IS_USER_LOGGED_IN,false);
    }

    fun setAuthenticationToken(token: String) {
        with(pref.edit()) {
            putString(TOKEN, token)
            commit()
        }
        RetrofitClientInstance.getInstance(context!!)!!.initRetrofit()
    }

    fun getAuthenticationToken(): String {
        return pref.getString(TOKEN, "")!!
    }



    fun setFirstName(firstName: String?) {
        with(pref.edit()) {
            putString(FIRST_NAME, firstName)
            commit()
        }
    }

    fun getFirstName(): String {
        return pref.getString(FIRST_NAME, "")!!
    }


    fun getLastName(): String {
        return pref.getString(LAST_NAME, "")!!
    }

    fun setLastName(lastName: String?) {
        with(pref.edit()) {
            putString(LAST_NAME, lastName)
            commit()
        }
    }


    fun setEmail(email: String?) {
        with(pref.edit()) {
            putString(EMAIL, email)
            commit()
        }
    }

    fun getEmail(): String {
        return pref.getString(EMAIL, "")!!
    }

    fun setPhone(phone: String?) {
        with(pref.edit()) {
            putString(PHONE, phone)
            commit()
        }
    }

    fun getPhone(): String {
        return pref.getString(PHONE, "")
    }

    fun getGender(): String {
        return pref.getString(GENDER, "")!!
    }

    fun setGender(gender: String?) {
        with(pref.edit()) {
            putString(GENDER, gender)
            commit()
        }
    }


    fun getAge(): Int {
        return pref.getInt(AGE, 0)
    }

    fun setAge(age: Int) {
        with(pref.edit()) {
            putInt(AGE, age)
            commit()
        }
    }

    fun getInvisible(): Boolean {
        return pref.getBoolean(INVISIBLE, false)
    }

    fun setInvisible(invisible: Boolean) {
        with(pref.edit()) {
            putBoolean(INVISIBLE, invisible)
            commit()
        }
    }


    fun setProfileImage(profileImage: String) {
        with(pref.edit()) {
            putString(PROFILE_IMAGE, profileImage)
            commit()
        }
    }

    fun getProfileImage(): String {
        return pref.getString(PROFILE_IMAGE, "")
    }


    fun setAddressLine1(addresLine1: String?) {
        with(pref.edit()) {
            putString(ADDRESS_LINE_1, addresLine1)
            commit()
        }
    }

    fun getAddressLine1(): String {
        return pref.getString(ADDRESS_LINE_1, "")!!
    }

    fun setAddressLine2(addresLine2: String) {
        with(pref.edit()) {
            putString(ADDRESS_LINE_2, addresLine2)
            commit()
        }
    }

    fun getAddressLine2(): String {
        return pref.getString(ADDRESS_LINE_2, "")
    }

    fun setCountry(country: String?) {
        with(pref.edit()) {
            putString(COUNTRY, country)
            commit()
        }
    }

    fun getCountry(): String {
        return pref.getString(COUNTRY, "")!!
    }

    fun setCity(city: String?) {
        with(pref.edit()) {
            putString(CITY, city)
            commit()
        }
    }

    fun getCity(): String {
        return pref.getString(CITY, "")
    }

    fun setStateRegionProvince(str: String?) {
        with(pref.edit()) {
            putString(STATE_REGION_PROVINCE, str)
            commit()
        }
    }

    fun getStateRegionProvince(): String {
        return pref.getString(STATE_REGION_PROVINCE, "")!!
    }

    fun setZip(zip: String?) {
        with(pref.edit()) {
            putString(ZIP, zip)
            commit()
        }
    }

    fun getZip(): String {
        return pref.getString(ZIP, "")
    }

    fun setDeviceId(deviceId: String) {
        with(pref.edit()) {
            putString(DEVICE_ID, deviceId)
            commit()
        }
    }

    fun getDeviceId(): String {
        return pref.getString(DEVICE_ID, "")!!
    }

    fun setDeviceToken(deviceId: String) {
        with(pref.edit()) {
            putString(DEVICE_TOKEN, deviceId)
            commit()
        }
    }

    fun getDeviceToken(): String {
        return pref.getString(DEVICE_TOKEN, "")
    }


    fun setCartOrderId(orderId: String, umeid: String) {
        with(pref.edit()) {
            putString(CART_ORDER_ID + "_" + umeid , orderId)
            commit()
        }
    }

    fun getCartOrderId(umeid:String): String {
        return pref.getString(CART_ORDER_ID + "_" + umeid, "")
    }

    fun getCurrency(): String{
        return "AUD"
    }

    fun getCurrencyAbr(): String{
        return "$"
    }

    fun logout(){
        setAuthenticationToken("")
        setFirstName("")
        setLastName("")
        setGender("")
        setAge(0)
        setEmail("")
        setPhone("")
        setProfileImage("")
        setAddressLine1("")
        setAddressLine2("")
        setCountry("")
        setCity("")
        setStateRegionProvince("")
        setZip("")
        setInvisible(false)
    }

    fun redirectToLogin(context: Context, message: String){
        val intent = Intent(context, LandingActivity::class.java)
        intent.putExtra(LandingActivity.SKIP_SPLASH,true)
        intent.putExtra(LandingActivity.START_UP_MESSAGe,message)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun getUser(): User {
        val user = User()
        user.firstName = getFirstName()
        user.lastName = getLastName()
        user.email = getEmail()
        user.phone = getPhone()
        user.gender = getGender()
        user.age = getAge()
        user.invisible = getInvisible()

       /* if(!getProfileImage().isNullOrEmpty()){
            val profilePicture = Media()
            profilePicture.id = ""
            profilePicture.location = getProfileImage()
            user.profilePicture = profilePicture
        }*/

       // user.shippingAddress = Address(getAddressLine1(),getAddressLine2(),getCity(),getCountry(),getStateRegionProvince(),getZip())

        return user
    }

    fun setUser(u: User) {
        setFirstName(u.firstName)
        setLastName(u.lastName)
        setGender(u.gender)
        setAge(u.age)
        setEmail(u.email)
        setPhone(u.phone)
        setInvisible(u.invisible)

       /* if(u.shippingAddress != null){
            setAddressLine1(u.shippingAddress!!.address1)
            setAddressLine2(u.shippingAddress!!.address2)
            setCountry(u.shippingAddress!!.country)
            setCity(u.shippingAddress!!.city)
            setStateRegionProvince(u.shippingAddress!!.state)
            setZip(u.shippingAddress!!.zip)
        }else{
            setAddressLine1("")
            setAddressLine2("")
            setCountry("")
            setCity("")
            setStateRegionProvince("")
            setZip("")
        }*/

      /*  if(u.profilePicture != null)
            setProfileImage(u.profilePicture!!.location)
        else
            setProfileImage("")*/

    }

    companion object {
        val PREF_NAME: String = "app_pref"
        val TOKEN: String = "authentication_token"
        val IS_USER_LOGGED_IN: String = "is_user_logged_id"
        val FIRST_NAME: String = "first_name"
        val LAST_NAME: String = "last_name"
        val EMAIL: String = "email"
        val PHONE: String = "phone"
        val GENDER: String = "gender"
        val AGE: String = "age"
        val PROFILE_IMAGE: String = "profile_image"
        val INVISIBLE: String = "invisible"


        val ADDRESS_LINE_1: String = "address_line_1"
        val ADDRESS_LINE_2: String = "address_line_2"
        val COUNTRY: String = "country"
        val CITY: String = "city"
        val STATE_REGION_PROVINCE = "state/region/province"
        val ZIP = "ZIP"

        val DEVICE_ID = "deviceid"
        val DEVICE_TOKEN = "devicetoken"
        val CART_ORDER_ID = "cartorderid"
        val PIMP_ID = "pimp_id"
        val LOCALE = "locale"
    }

}