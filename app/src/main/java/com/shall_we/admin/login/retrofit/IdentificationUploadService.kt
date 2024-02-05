package com.shall_we.admin.login.retrofit

import android.util.Log
import com.google.gson.JsonElement
import com.shall_we.admin.login.data.IdentificationUploadReq
import com.shall_we.admin.retrofit.API
import com.shall_we.admin.retrofit.BodyData
import com.shall_we.admin.retrofit.IRetrofit
import com.shall_we.admin.retrofit.RESPONSE_STATE
import com.shall_we.admin.retrofit.RetrofitClient
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class IdentificationUploadService {

    private val iRetrofit: IRetrofit? =
        RetrofitClient.getClient(API.BASE_URL)?.create(IRetrofit::class.java)

    private val iRetrofit2: IRetrofit? =
            RetrofitClient.getClient2("https://668h6987ib.execute-api.ap-northeast-2.amazonaws.com")?.create(IRetrofit::class.java)

    suspend fun getImgUrl(data: BodyData): Triple<RESPONSE_STATE, String, String> = suspendCoroutine { continuation ->
        val call = iRetrofit2?.getImgUrl(data) ?: return@suspendCoroutine

        call.enqueue(object : retrofit2.Callback<JsonElement> {
            // 응답 성공
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d("retrofit", "RetrofitManager - onResponse() called / response : ${response.code()}")

                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            val body = it.asJsonObject
                            val imageKey = body.get("imageKey").toString()
                            val presignedUrl = body.get("presignedUrl").toString()
                            continuation.resume(Triple(RESPONSE_STATE.OKAY, imageKey, presignedUrl))
                            Log.d("retrofit", "RetrofitManager - getImgUrl onResponse() called / response : ${response.code()}")
                        }
                    }
                }
            }

            // 응답 실패
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d("retrofit", "RetrofitManager - onFailure() called / t: $t")
                continuation.resume(Triple(RESPONSE_STATE.FAIL, "null", "null"))
            }
        })
    }


    fun uploadImg(data: RequestBody, url: String, endpoint :String,  completion:(RESPONSE_STATE, Int?) -> Unit){
        Log.d("presignedUrl",endpoint)
        RetrofitClient.getClient2(url)?.create(IRetrofit::class.java)?.uploadImg(url = endpoint, filebody = data)?.enqueue(object:
                Callback<JsonElement> {
            // 응답 성공인 경우
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(
                        "retrofit",
                        "RetrofitManager - onResponse() called / response : ${response.code()}"
                )
                completion(RESPONSE_STATE.OKAY, response.code())

            }

            // 응답 실패인 경우
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d("retrofit", "RetrofitManager - onFailure() called / t: $t")
                completion(RESPONSE_STATE.FAIL, null)
            }
        })
    }

    fun postIdenficicationUpload(uploadImage: IdentificationUploadReq, completion: (RESPONSE_STATE, Int?) -> Unit){
        iRetrofit?.postIdenficicationUpload(identification = uploadImage)?.enqueue(object:
            Callback<JsonElement> {
            // 응답 성공인 경우
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(
                    "retrofit",
                    "RetrofitManager - onResponse() called / response : ${response.code()}"
                )
                completion(RESPONSE_STATE.OKAY, response.code())

            }

            // 응답 실패인 경우
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d("retrofit", "RetrofitManager - onFailure() called / t: $t")
                completion(RESPONSE_STATE.FAIL, null)
            }
        })
    }
}