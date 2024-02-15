package com.shall_we.admin.reservation.data

import com.google.gson.annotations.SerializedName

data class ReservationData(
    @SerializedName("experienceGiftId")
    val experienceGiftId: Long,

    @SerializedName("subtitle")
    val description: String,

    @SerializedName("title")
    val title: String
)
