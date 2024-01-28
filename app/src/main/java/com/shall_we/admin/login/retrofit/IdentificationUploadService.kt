package com.shall_we.admin.login.retrofit

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.shall_we.admin.login.data.AuthRes
import com.shall_we.admin.login.data.ErrorRes
import com.shall_we.admin.login.data.IdentificationUploadReq
import com.shall_we.admin.login.data.SignInReq
import com.shall_we.admin.retrofit.API
import com.shall_we.admin.retrofit.IRetrofit
import com.shall_we.admin.retrofit.RESPONSE_STATE
import com.shall_we.admin.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class IdentificationUploadService {

    private val iRetrofit: IRetrofit? =
        RetrofitClient.getClient(API.BASE_URL)?.create(IRetrofit::class.java)

    fun postIdenficicationUpload(uploadImage: IdentificationUploadReq, completion: (RESPONSE_STATE, Int?) -> Unit){
        iRetrofit?.postIdenficicationUpload(uploadImage)?.enqueue(object:
            Callback<JsonElement> {
            // 응답 성공인 경우
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(
                    "retrofit",
                    "RetrofitManager - onResponse() called / response : ${response.code()}"
                )
                completion(RESPONSE_STATE.OKAY, response.code())

            }

            // 응답 실패인 경우
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d("retrofit", "RetrofitManager - onFailure() called / t: $t")
                completion(RESPONSE_STATE.FAIL, null)
            }
        })
    }
}