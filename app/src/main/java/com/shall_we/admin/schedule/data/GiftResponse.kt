package com.shall_we.admin.schedule.data

import com.google.gson.annotations.SerializedName

data class GiftResponse(
    @SerializedName("data")
    val data: List<ScheduleData>
)

