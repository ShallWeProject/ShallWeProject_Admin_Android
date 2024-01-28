package com.shall_we.admin.schedule.data

import com.google.gson.annotations.SerializedName

data class ScheduleData(
    @SerializedName("experienceGiftId")
    val experienceGiftId: Long,

    @SerializedName("subtitle")
    val subtitle: String,

    @SerializedName("title")
    val title: String
)

