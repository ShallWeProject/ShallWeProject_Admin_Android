package com.shall_we.admin.login.data

import com.google.gson.annotations.SerializedName

data class MessageRes(
    @SerializedName("timestamp")
    val timestamp: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("code")
    val code: String,
    @SerializedName("status")
    val status: Int,
    @SerializedName("class")
    val errorResponse: String,
    @SerializedName("errors")
    val errors: List<String>?
)