package com.shall_we.admin.login.retrofit

import android.util.Log
import com.google.gson.Gson
import com.shall_we.admin.login.data.AuthRes
import com.shall_we.admin.login.data.ErrorRes
import com.shall_we.admin.login.data.MessageRes
import com.shall_we.admin.login.data.SignUpReq
import com.shall_we.admin.retrofit.API
import com.shall_we.admin.retrofit.IRetrofit
import com.shall_we.admin.retrofit.RESPONSE_STATE
import com.shall_we.admin.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpService() {
    private val iRetrofit: IRetrofit? =
        RetrofitClient.getClient(API.BASE_URL)?.create(IRetrofit::class.java)


    fun postAuthSignUp(auth: SignUpReq,completion: (RESPONSE_STATE, Int?,AuthRes?) -> Unit){
        iRetrofit?.authSignUp(SignUpReq(auth.name,auth.phoneNumber,auth.password,auth.marketingConsent))?.enqueue(object: Callback<AuthRes> {
            override fun onResponse(call: Call<AuthRes>, response: Response<AuthRes>) {
                Log.d("signup",response.code().toString())
                completion(RESPONSE_STATE.OKAY, response.code(),response.body())
            }

            override fun onFailure(call: Call<AuthRes>, t: Throwable) {
                completion(RESPONSE_STATE.FAIL, null, null)

            }
        })
    }


}