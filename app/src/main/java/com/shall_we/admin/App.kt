package com.shall_we.admin

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

class App : Application() {

    companion object{
        lateinit var instance: App
        lateinit var context: Context

        lateinit var sharedPreferences: SharedPreferences

        var accessToken:String? = "access_token"
        var refreshToken:String? = "refresh_token"
        var phoneNumber : String? = "phone_number"
        var password : String? = "password"
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        context = applicationContext

        sharedPreferences =
            applicationContext.getSharedPreferences("com.shall_we.admin", MODE_PRIVATE)
    }

}