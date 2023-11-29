package com.shall_we.admin.product.data

import com.google.gson.annotations.SerializedName

data class AdminExperienceRes(
    @SerializedName("experienceGiftId")
    val experienceGiftId: Int,
    @SerializedName("subtitle")
    val subtitle: String,
    @SerializedName("title")
    val title: String,
)
