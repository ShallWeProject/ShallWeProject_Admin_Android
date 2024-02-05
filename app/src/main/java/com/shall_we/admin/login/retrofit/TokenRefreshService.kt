package com.shall_we.admin.login.retrofit

import android.util.Log
import com.shall_we.admin.login.data.AuthRes
import com.shall_we.admin.login.data.AuthTokenData
import com.shall_we.admin.login.data.RefreshTokenReq
import com.shall_we.admin.retrofit.API
import com.shall_we.admin.retrofit.IRetrofit
import com.shall_we.admin.retrofit.RESPONSE_STATE
import com.shall_we.admin.retrofit.RetrofitClient
import com.shall_we.admin.retrofit.TokenAuthenticator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TokenRefreshService(val authSignInterface: TokenAuthenticator) {
    private val iRetrofit: IRetrofit? =
        RetrofitClient.getClient(API.BASE_URL)?.create(IRetrofit::class.java)

    fun tokenRefresh(refreshToken: RefreshTokenReq, completion: (RESPONSE_STATE, AuthTokenData?) -> Unit) {
        val call = iRetrofit?.tokenRefresh(refreshToken) ?: return
        call.enqueue(object : Callback<AuthRes> {
            // 응답 성공인 경우
            override fun onResponse(call: Call<AuthRes>, response: Response<AuthRes>) {
                if(response.code() == 200){
                    val authResponse = response.body()
                    if (authResponse != null) {
                        Log.e("login", "Success: ${authResponse}")
                        completion(RESPONSE_STATE.OKAY, authResponse.data)
                    } else {
                        completion(RESPONSE_STATE.OKAY, null)

                    }
                }else{
                    try{
                        Log.e("login", "Request failed with response code: ${response.code()}")

                        completion(RESPONSE_STATE.OKAY, null)

                    }catch(e:Exception){
                        completion(RESPONSE_STATE.OKAY, null)

                    }
                }
            }

            override fun onFailure(call: Call<AuthRes>, t: Throwable) {
            }
        })
    }
}