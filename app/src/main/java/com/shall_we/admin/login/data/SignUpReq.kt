package com.shall_we.admin.login.data

data class SignUpReq(
    var name: String,
    var phoneNumber: String,
    var password: String,
    var marketingConsent: Boolean
)
