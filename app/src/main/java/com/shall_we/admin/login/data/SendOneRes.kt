package com.shall_we.admin.login.data

import com.google.gson.annotations.SerializedName

data class SendOneRes(
    @SerializedName("data")
    val data: SendOneData,
    @SerializedName("transaction_time")
    val transactionTime: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("statusCode")
    val statusCode: Int
)

data class SendOneData(
    @SerializedName("requestId")
    val requestId: String,
    @SerializedName("requestTime")
    val requestTime: String,
    @SerializedName("statusCode")
    val statusCode: String,
    @SerializedName("statusName")
    val statusName: String
)