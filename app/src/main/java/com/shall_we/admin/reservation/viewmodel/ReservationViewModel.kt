package com.shall_we.admin.reservation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shall_we.admin.reservation.data.ReservationListData
import com.shall_we.admin.reservation.data.ReservationResponse
import com.shall_we.admin.retrofit.API
import com.shall_we.admin.retrofit.IRetrofit
import com.shall_we.admin.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReservationViewModel: ViewModel() {
    private val _reservations = MutableLiveData<List<ReservationListData>>()
    val reservations: LiveData<List<ReservationListData>> = _reservations

    fun fetchReservations(giftId: Long, date: String) {
        val iRetrofit: IRetrofit? = RetrofitClient.getClient(API.BASE_URL)?.create(IRetrofit::class.java)
        iRetrofit?.getReservationsByDate(giftId, date)?.enqueue(object : Callback<ReservationResponse> {
            override fun onResponse(
                call: Call<ReservationResponse>,
                response: Response<ReservationResponse>
            ) {
                if (response.isSuccessful) {
                    val newReservations = response.body()
                    newReservations?.let {
                        _reservations.value = it.data
                        Log.d("reservationss",_reservations.value.toString())
                    }
                } else {
                    Log.d("ReservationError", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ReservationResponse>, t: Throwable) {
                Log.d("NetworkError", "Error: ${t.message}")
            }
        })
    }

}
