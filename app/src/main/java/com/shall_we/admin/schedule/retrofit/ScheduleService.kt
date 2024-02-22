import android.util.Log
import com.google.gson.JsonElement
import com.shall_we.admin.retrofit.API
import com.shall_we.admin.retrofit.IRetrofit
import com.shall_we.admin.retrofit.RESPONSE_STATE
import com.shall_we.admin.retrofit.RetrofitClient
import com.shall_we.admin.schedule.data.GiftResponse
import com.shall_we.admin.schedule.data.ReservationInfo
import com.shall_we.admin.schedule.data.ScheduleData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScheduleService {
    private val iRetrofit: IRetrofit? =
        RetrofitClient.getClient(API.BASE_URL)?.create(IRetrofit::class.java)

    fun getGift(completion: (RESPONSE_STATE, List<ScheduleData>?) -> Unit) {
        val call = iRetrofit?.getGift()
        call?.enqueue(object : Callback<GiftResponse> {
            override fun onResponse(call: Call<GiftResponse>, response: Response<GiftResponse>) {
                if (response.isSuccessful) {
                    val giftList = response.body()?.data
                    completion(RESPONSE_STATE.OKAY, giftList)

                } else {
                    completion(RESPONSE_STATE.FAIL, null)

                }
            }
            override fun onFailure(call: Call<GiftResponse>, t: Throwable) {
                completion(RESPONSE_STATE.FAIL, null)

            }
        })
    }



    fun addReservation(reservationInfo: ReservationInfo, completion: (RESPONSE_STATE, JsonElement?) -> Unit) {
        val call = iRetrofit?.AddReservation(reservationInfo)
        call?.enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    Log.d("is",result.toString())
                    completion(RESPONSE_STATE.OKAY, result)
                } else {
                    completion(RESPONSE_STATE.FAIL, null)
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                completion(RESPONSE_STATE.FAIL, null)
            }
        })
    }


}
