package com.shall_we.admin.login.data

import com.google.gson.annotations.SerializedName

data class ErrorRes (
    @SerializedName("check")
    val check: Boolean,
    @SerializedName("information")
    val information: Information
)

data class Information(
    @SerializedName("timestamp")
    val timestamp: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("code")
    val code: Any?, // code가 null이 될 수 있으므로 Any?로 정의
    @SerializedName("status")
    val status: Int,
    @SerializedName("class")
    val classInfo: Any?, // class가 null이 될 수 있으므로 Any?로 정의
    @SerializedName("errors")
    val errors: List<Any> // errors가 빈 배열일 수 있으므로 List<Any>로 정의
)

