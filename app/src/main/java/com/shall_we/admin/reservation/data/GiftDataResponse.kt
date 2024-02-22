package com.shall_we.admin.reservation.data

import com.google.gson.annotations.SerializedName
import com.shall_we.admin.schedule.data.ScheduleData

data class GiftDataResponse(
    @SerializedName("data")
    val data: List<ReservationData>

)
