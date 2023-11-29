package com.shall_we.admin.product.data

import com.google.gson.annotations.SerializedName
import java.util.Date

data class AdminMainRes(
    @SerializedName("currentDate")
    val currentDate: Date,
    @SerializedName("bookedReservationsCount")
    val bookedReservationsCount: Int,
)
