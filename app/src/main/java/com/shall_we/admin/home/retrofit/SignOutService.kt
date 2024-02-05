package com.shall_we.admin.home.retrofit

import android.util.Log
import com.shall_we.admin.home.data.AuthSignOutResponse
import com.shall_we.admin.login.data.RefreshTokenReq
import com.shall_we.admin.retrofit.API
import com.shall_we.admin.retrofit.IRetrofit
import com.shall_we.admin.retrofit.RESPONSE_STATE
import com.shall_we.admin.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignOutService() {

    private val iRetrofit: IRetrofit? =
        RetrofitClient.getClient(API.BASE_URL)?.create(IRetrofit::class.java)

    fun postSignOut(refreshToken: RefreshTokenReq, completion: (RESPONSE_STATE, AuthSignOutResponse?) -> Unit) {
        val call = iRetrofit?.authSignOut(refreshToken) ?: return
        call.enqueue(object : Callback<AuthSignOutResponse> {
            // 응답 성공인 경우
            override fun onResponse(call: Call<AuthSignOutResponse>, response: Response<AuthSignOutResponse>) {
                if(response.code() == 200){
                    val authResponse = response.body()
                    if (authResponse != null) {
                        Log.e("logout", "Success: ${authResponse}")
                        completion(RESPONSE_STATE.OKAY, authResponse)
                    } else {
                        completion(RESPONSE_STATE.FAIL, null)
                    }
                }else{
                    try{
                        Log.e("logout", "Request failed with response code: ${response.code()}")
                        completion(RESPONSE_STATE.FAIL, null)
                    }catch(e:Exception){
                        completion(RESPONSE_STATE.FAIL, null)

                    }
                }
            }

            override fun onFailure(call: Call<AuthSignOutResponse>, t: Throwable) {
            }
        })
    }
}