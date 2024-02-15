package com.shall_we.admin.reservation.retrofit

import android.util.Log
import com.shall_we.admin.reservation.data.GiftDataResponse
import com.shall_we.admin.reservation.data.MessageRes
import com.shall_we.admin.reservation.data.ReservationData
import com.shall_we.admin.retrofit.API
import com.shall_we.admin.retrofit.IRetrofit
import com.shall_we.admin.retrofit.RESPONSE_STATE
import com.shall_we.admin.retrofit.RetrofitClient
import com.shall_we.admin.schedule.data.ScheduleData
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
                        Log.d("newreservation", "Success: ${authResponse}")
                        completion(RESPONSE_STATE.OKAY, authResponse)
                    } else {
                        Log.d("newreservation", "Request failed with response code: ${response.code()}")
                        completion(RESPONSE_STATE.FAIL, null)
                    }
                }else{
                    try{
                        Log.d("newreservation", "Request failed with response code: ${response.code()}")
                        completion(RESPONSE_STATE.FAIL, null)

                    }catch(e:Exception){
                        completion(RESPONSE_STATE.FAIL, null)
                    }
                }
            }

            // 응답 실패인 경우
            override fun onFailure(call: Call<MessageRes>, t: Throwable) {
                Log.e("new", "Request failed with error: ${t.message}")
                t.printStackTrace()
                completion(RESPONSE_STATE.FAIL, null)
            }
        })
    }

    fun getReservationGift(completion: (RESPONSE_STATE, List<ReservationData>?) -> Unit) {
        val call = iRetrofit?.getReservationGift()
        call?.enqueue(object : Callback<GiftDataResponse> {
            override fun onResponse(call: Call<GiftDataResponse>, response: Response<GiftDataResponse>) {
                if (response.isSuccessful) {
                    Log.d("check",response.body().toString())
                    val giftList = response.body()
                    completion(RESPONSE_STATE.OKAY, giftList?.data)
                } else {
                    completion(RESPONSE_STATE.FAIL, null)
                }
            }

            override fun onFailure(call: Call<GiftDataResponse>, t: Throwable) {
                completion(RESPONSE_STATE.FAIL, null)
            }
        })
    }

}
