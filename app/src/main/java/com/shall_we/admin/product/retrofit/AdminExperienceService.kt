package com.shall_we.admin.product.retrofit

import android.util.Log
import com.google.gson.JsonElement
import com.shall_we.admin.product.ProductData
import com.shall_we.admin.product.data.AdminExperienceReq
import com.shall_we.admin.retrofit.API
import com.shall_we.admin.retrofit.IRetrofit
import com.shall_we.admin.retrofit.RESPONSE_STATE
import com.shall_we.admin.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminExperienceService {

    private val iRetrofit: IRetrofit? =
        RetrofitClient.getClient(API.BASE_URL)?.create(IRetrofit::class.java)

    fun adminExperienceGift(
        completion: (RESPONSE_STATE, ArrayList<ProductData>?) -> Unit
    ) {
        val call = iRetrofit?.adminExperienceGift() ?: return
        call.enqueue(object : Callback<JsonElement> {
            // 응답 성공인 경우
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(
                    "retrofit",
                    "RetrofitManager - onResponse() called / response : ${response.code()}"
                )
                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            var parsedProductDataArray = ArrayList<ProductData>()
                            val body = it.asJsonObject

                            Log.d("JSON Object", "${body}")

                            val gifts = body.getAsJsonArray("data")
                            gifts.forEach { resultItem ->
                                val resultItemObject = resultItem.asJsonObject

                                val experienceGiftId =
                                    resultItemObject.get("experienceGiftId").asInt
                                val subtitle = resultItemObject.get("subtitle").asString
                                val title = resultItemObject.get("title").asString
                                val giftItem = ProductData(
                                    experienceGiftId = experienceGiftId,
                                    subtitle = subtitle,
                                    title = title
                                )
                                Log.d("gift - send result: ", "$giftItem")
                                parsedProductDataArray.add(giftItem)
                            }
                            completion(RESPONSE_STATE.OKAY, parsedProductDataArray)
                        }
                    }
                }
            }

            // 응답 실패인 경우
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d("retrofit", "RetrofitManager - onFailure() called / t: $t")
                completion(RESPONSE_STATE.FAIL, null)
            }

        })
    }

    fun postAdminExperienceGift(
        adminExperienceReq: AdminExperienceReq,
        completion: (RESPONSE_STATE) -> Unit
    ) {
        val call =
            iRetrofit?.postAdminExperienceGiftRegister(adminExperienceReq = adminExperienceReq)
                ?: return

        call.enqueue(object : Callback<JsonElement> {
            // 응답 성공
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(
                    "retrofit",
                    "RetrofitManager - onResponse() called / response : ${response.code()}"
                )
                Log.d("array", "$adminExperienceReq")
                completion(RESPONSE_STATE.OKAY)
            }

            // 응답 실패
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d("retrofit", "RetrofitManager - onFailure() called / t: $t")
                completion(RESPONSE_STATE.FAIL)
            }
        })
    }

    fun putAdminExperienceGift(
        experienceGiftId: Int, adminExperienceReq: AdminExperienceReq,
        completion: (RESPONSE_STATE) -> Unit
    ) {
        val call =
            iRetrofit?.putAdminExperienceGift(experienceGiftId = experienceGiftId, adminExperienceReqArray = adminExperienceReq)
                ?: return

        call.enqueue(object : Callback<JsonElement> {
            // 응답 성공
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(
                    "retrofit",
                    "RetrofitManager - onResponse() called / response : ${response.code()}"
                )

                when (response.code()) {
                    200 -> {
                        completion(RESPONSE_STATE.OKAY)
                    }
                }
            }

            // 응답 실패
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d("retrofit", "RetrofitManager - onFailure() called / t: $t")
                completion(RESPONSE_STATE.FAIL)
            }
        })
    }

    fun delAdminExperienceGiftRegister(
        experienceGiftId: Int,
        completion: (RESPONSE_STATE) -> Unit
    ) {
        val call =
            iRetrofit?.delAdminExperienceGift(experienceGiftId = experienceGiftId)
                ?: return

        call.enqueue(object : Callback<JsonElement> {
            // 응답 성공
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(
                    "retrofit",
                    "RetrofitManager - onResponse() called / response : ${response.code()}"
                )

                when (response.code()) {
                    200 -> {
                            completion(RESPONSE_STATE.OKAY)
                        }
                    }
                }

            // 응답 실패
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d("retrofit", "RetrofitManager - onFailure() called / t: $t")
                completion(RESPONSE_STATE.FAIL)
            }
        })
    }

}
