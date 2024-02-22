package com.shall_we.admin.login.retrofit

import android.util.Log
import com.google.gson.JsonElement
import com.shall_we.admin.login.data.SendOneReq
import com.shall_we.admin.retrofit.API
import com.shall_we.admin.retrofit.IRetrofit
import com.shall_we.admin.retrofit.RESPONSE_STATE
import com.shall_we.admin.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PhoneAuthService() {
    private val iRetrofit: IRetrofit? =
        RetrofitClient.getClient(API.BASE_URL)?.create(IRetrofit::class.java)


    fun sendOne(phoneNumber: SendOneReq, completion: (RESPONSE_STATE, JsonElement?) -> Unit) {
        val call = iRetrofit?.sendOne(phoneNumber) ?: return
        call.enqueue(object : Callback<JsonElement> {
            // 응답 성공인 경우
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(
                    "retrofit",
                    "RetrofitManager - onResponse() called / response : ${response.message()}"
                )

                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            completion(RESPONSE_STATE.OKAY, it)
                        }
                    }
                }
            }

            // 응답 실패인 경우
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d("retrofit", "RetrofitManager - onFailure() called / t: $t")
                completion(RESPONSE_STATE.FAIL, null)
            }
        })
    }

}