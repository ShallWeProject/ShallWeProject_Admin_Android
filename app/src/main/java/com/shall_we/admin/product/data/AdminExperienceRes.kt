package com.shall_we.admin.product.data

import com.google.gson.annotations.SerializedName

data class AdminExperienceRes(
    @SerializedName("data")
    val data: Product,
    @SerializedName("transaction_time")
    val transactionTime: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("statusCode")
    val statusCode: Int
)

data class Product(
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
    val explanation: List<ExplanationRes>,
    @SerializedName("location")
    val location: String,
    @SerializedName("price")
    val price: Int? = 0,
    @SerializedName("note")
    val note: String,
    @SerializedName("experienceGiftId")
    val experienceGiftId: Int
)