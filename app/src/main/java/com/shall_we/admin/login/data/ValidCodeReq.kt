package com.shall_we.admin.login.data

data class ValidCodeReq(
    val verificationCode: String,
    val phoneNumber: String
)
