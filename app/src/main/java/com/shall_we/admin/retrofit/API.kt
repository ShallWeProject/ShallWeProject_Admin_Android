package com.shall_we.admin.retrofit

object API {
    const val BASE_URL : String = "http://13.124.44.109/"

    const val AUTH_SIGN_UP : String = "auth/shop-owner/sign-up"

    const val AUTH_SIGN_OUT : String = "/auth/shop-owner/sign-out"

    const val AUTH_SIGN_IN : String = "/auth/shop-owner/sign-in"

    const val PATCH_SHOP_OWNER : String = "/api/v1/shop-owners"

    const val AUTH_REFRESH = "auth/refresh"

    const val SEND_ONE = "auth/send-one"

    const val VALID_VERIFICATION = "auth/valid-verification-code"


}

enum class RESPONSE_STATE {
    OKAY,
    FAIL
}