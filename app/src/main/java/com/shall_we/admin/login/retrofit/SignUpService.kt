package com.shall_we.admin.login.retrofit

import android.util.Log
import com.google.gson.Gson
import com.shall_we.admin.login.data.AuthRes
import com.shall_we.admin.login.data.ErrorRes
import com.shall_we.admin.login.data.MessageRes
import com.shall_we.admin.login.data.SignUpReq
import com.shall_we.admin.retrofit.API
import com.shall_we.admin.retrofit.IRetrofit
import com.shall_we.admin.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpService(val authSignInterface: IAuthSignUp) {
    private val iRetrofit: IRetrofit? =
        RetrofitClient.getClient(API.BASE_URL)?.create(IRetrofit::class.java)


    fun postAuthSignUp(auth: SignUpReq){
        iRetrofit?.authSignUp(SignUpReq(auth.name,auth.phoneNumber,auth.password,auth.marketingConsent))?.enqueue(object: Callback<MessageRes> {
            override fun onResponse(call: Call<MessageRes>, response: Response<MessageRes>) {
                if(response.code() == 200){
                    val authResponse = response.body()
                    if (authResponse != null) {
                        Log.e("login", "Success: ${authResponse}")
                        authSignInterface.onPostAuthSignUpSuccess(authResponse)
                    } else {
                        authSignInterface.onPostAuthSignUpFailed("응답 데이터를 받아올 수 없습니다.")
                    }
                }
                else if(response.code() == 400){
                    Log.e("login", "singup failed")
                    authSignInterface.onPostAuthSignUpFailed("로그인")
                }
                else{
                    try{
                        val gson = Gson()
                        val errorResponse =
                            gson.fromJson(response.errorBody()?.string(), ErrorRes::class.java)
                        authSignInterface.onPostAuthSignUpFailed("회원가입실패")
                    }catch(e:Exception){
                        authSignInterface.onPostAuthSignUpFailed(e.message?:"통신 오류")
                    }
                }
            }

            override fun onFailure(call: Call<MessageRes>, t: Throwable) {
                authSignInterface.onPostAuthSignUpFailed(t.message?:"통신 오류")
            }
        })
    }


}