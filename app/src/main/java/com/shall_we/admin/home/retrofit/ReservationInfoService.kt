package com.shall_we.admin.home.retrofit

import android.util.Log
import com.shall_we.admin.home.data.ReservationInfoData
import com.shall_we.admin.home.data.ReservationInfoRes
import com.shall_we.admin.login.data.MessageRes
import com.shall_we.admin.login.data.RefreshTokenReq
import com.shall_we.admin.retrofit.API
import com.shall_we.admin.retrofit.IRetrofit
import com.shall_we.admin.retrofit.RESPONSE_STATE
import com.shall_we.admin.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReservationInfoService {

    private val iRetrofit: IRetrofit? =
        RetrofitClient.getClient(API.BASE_URL)?.create(IRetrofit::class.java)

    fun reservationInfo(completion: (RESPONSE_STATE, ReservationInfoRes?) -> Unit) {
        val call = iRetrofit?.homeReservationInfo() ?: return
        call.enqueue(object : Callback<ReservationInfoRes> {
            // 응답 성공인 경우
            override fun onResponse(call: Call<ReservationInfoRes>, response: Response<ReservationInfoRes>) {
                if(response.code() == 200){
                    val authResponse = response.body()
                    if (authResponse != null) {
                        Log.e("login", "Success: ${authResponse}")
                        completion(RESPONSE_STATE.OKAY, response.body())
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

            override fun onFailure(call: Call<ReservationInfoRes>, t: Throwable) {
            }
        })
    }
}