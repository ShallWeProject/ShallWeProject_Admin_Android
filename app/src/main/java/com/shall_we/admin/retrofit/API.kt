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

    const val ADMIN_EXPERIENCE_GIFT = "/api/v1/admin/experience/gift"

    const val CHANGE_PASSWORD = "auth/shop-owner/change-password"

    const val HOME_RESERVATION_INFO = "api/v1/admin/experience/gift/main"

    const val ADMIN_EXPERIENCE_GIFT_MAIN = "/api/v1/admin/experience/gift/main"

    const val ADMIN_EXPERIENCE_GIFT_REGISTER = "/api/v1/admin/experience/gift/register"

    const val ADMIN_EXPERIENCE_GIFT_DELETE = "/api/v1/admin/experience/gift/{experienceGiftId}"
    //일정관리
    const val ADMIN_MANAGING_RESERVATION="/api/v1/shop-owners/"

    //예약추가
    const val ADMIN_ADD_RESERVATION= "/api/v1/shop-owners/"

    //해당 날짜에 생성된 예약 조회
    const val ADMIN_GET_RESERVATION="/api/v1/shop-owners/date"
}

enum class RESPONSE_STATE {
    OKAY,
    FAIL
}