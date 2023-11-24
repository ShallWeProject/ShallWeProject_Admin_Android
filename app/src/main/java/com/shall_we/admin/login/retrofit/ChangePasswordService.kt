package com.shall_we.admin.login.retrofit

import android.util.Log
import com.google.gson.JsonElement
import com.shall_we.admin.login.data.ChangePasswordReq
import com.shall_we.admin.login.data.MessageRes
import com.shall_we.admin.retrofit.API
import com.shall_we.admin.retrofit.IRetrofit
import com.shall_we.admin.retrofit.RESPONSE_STATE
import com.shall_we.admin.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordService {

    private val iRetrofit: IRetrofit? =
        RetrofitClient.getClient(API.BASE_URL)?.create(IRetrofit::class.java)

    fun patchChangePassword(changePassword : ChangePasswordReq, completion: (RESPONSE_STATE, Int?) -> Unit) {
        val call = iRetrofit?.changePassword(changePassword) ?: return
        call.enqueue(object : Callback<JsonElement> {
            // 응답 성공인 경우
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if(response.code() == 200){
                    completion(RESPONSE_STATE.OKAY, response.code())

                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                completion(RESPONSE_STATE.FAIL, null)

            }
        })
    }
}