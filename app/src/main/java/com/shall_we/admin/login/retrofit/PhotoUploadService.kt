package com.shall_we.admin.login.retrofit

import android.util.Log
import com.google.gson.JsonElement
import com.shall_we.admin.login.data.UploadPhotoArray
import com.shall_we.admin.retrofit.API
import com.shall_we.admin.retrofit.IRetrofit
import com.shall_we.admin.retrofit.RESPONSE_STATE
import com.shall_we.admin.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Response

class PhotoUploadService {

    private val iRetrofit: IRetrofit? =
        RetrofitClient.getClient(API.BASE_URL)?.create(IRetrofit::class.java)

}