package com.shall_we.admin.reservation.retrofit

import com.shall_we.admin.login.data.MessageRes

import android.util.Log
import com.shall_we.admin.retrofit.API
import com.shall_we.admin.retrofit.IRetrofit
import com.shall_we.admin.retrofit.RESPONSE_STATE
import com.shall_we.admin.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReservationService {

    private val iRetrofit: IRetrofit? =
        RetrofitClient.getClient(API.BASE_URL)?.create(IRetrofit::class.java)

    fun postReservation(reservationId: Long, completion: (RESPONSE_STATE, MessageRes?) -> Unit) {
        val call = iRetrofit?.postReservation(reservationId) ?: return
        call.enqueue(object : Callback<MessageRes> {
            // 응답 성공인 경우
            override fun onResponse(call: Call<MessageRes>, response: Response<MessageRes>) {
                if(response.code() == 200){
                    val authResponse = response.body()
                    if (authResponse != null) {
                        Log.e("reservation", "Success: ${authResponse}")
                        completion(RESPONSE_STATE.OKAY, authResponse)
                    } else {
                        completion(RESPONSE_STATE.OKAY, null)
                    }
                }else{
                    try{
                        Log.e("reservation", "Request failed with response code: ${response.code()}")

                        completion(RESPONSE_STATE.OKAY, null)

                    }catch(e:Exception){
                        completion(RESPONSE_STATE.OKAY, null)

                    }
                }
            }

            // 응답 실패인 경우
            override fun onFailure(call: Call<MessageRes>, t: Throwable) {
                Log.e("reservation", "Request failed with error: ${t.message}")
                completion(RESPONSE_STATE.FAIL, null)
            }
        })
    }
}