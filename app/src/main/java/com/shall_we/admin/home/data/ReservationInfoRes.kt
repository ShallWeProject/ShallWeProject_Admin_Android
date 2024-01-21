package com.shall_we.admin.home.data

import com.google.gson.annotations.SerializedName

data class ReservationInfoRes(
    @SerializedName("data")
    val data: ReservationInfoData,
    @SerializedName("transaction_time")
    val transactionTime: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("statusCode")
    val statusCode: Int
)

data class ReservationInfoData(
    @SerializedName("currentDate")
    val currentDate: String,
    @SerializedName("bookedReservationsCount")
    val bookedReservationsCount: Int,
    @SerializedName("bookedCheckCount")
    val bookedCheckCount: Int
)
