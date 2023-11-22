package com.shall_we.admin

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

class App : Application() {

    companion object{
        lateinit var instance: App
        lateinit var context: Context

        var accessToken:String? = "ACCESS_TOKEN"
        var refreshToken:String? = "REFRESH_TOKEN"
        var phoneNumber : String? = "phoneNumber"
        var password : String? = "password"
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        context = applicationContext
    }

}