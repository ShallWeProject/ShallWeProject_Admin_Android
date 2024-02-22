package com.shall_we.admin.product.data

import com.google.gson.annotations.SerializedName

data class AdminExperienceReq(
    @SerializedName("subtitle")
    val subtitle: String, // 지역
    @SerializedName("expCategory")
    val expCategory: String,
    //val sttCategory: String,
    @SerializedName("title")
    val title: String, // 상품명
    @SerializedName("giftImgKey")
    val giftImgKey: List<String>?,// 대표사진
    @SerializedName("description")
    val description: String,
    @SerializedName("explanation")
    val explanation: List<ExplanationReq>?,
    @SerializedName("location")
    val location: String,
    @SerializedName("price")
    val price: Int? = 0,
    @SerializedName("note")
    val note: String
)

data class ExplanationReq (
    @SerializedName("stage")
    val stage: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("explanationKey")
    val explanationKey: String? = null
)