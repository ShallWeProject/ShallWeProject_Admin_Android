package com.shall_we.admin.retrofit

object API {
    const val BASE_URL : String = "https://api.shallwes.com/"

    const val AUTH_SIGN_UP : String = "auth/shop-owner/sign-up"

    const val AUTH_SIGN_OUT : String = "/auth/shop-owner/sign-out"

    const val AUTH_SIGN_IN : String = "/auth/shop-owner/sign-in"

    const val PATCH_SHOP_OWNER : String = "/api/v1/shop-owners"

    const val AUTH_REFRESH = "auth/refresh"

    const val SEND_ONE = "auth/send-one"

    const val VALID_VERIFICATION = "auth/valid-verification-code"

    const val CHANGE_PASSWORD = "auth/shop-owner/change-password"

    const val HOME_RESERVATION_INFO = "/api/v1/experience/gift/shop-owner/main"

    const val ADMIN_EXPERIENCE_GIFT_REGISTER = "/api/v1/experience/gift/shop-owner/register"

    const val ADMIN_EXPERIENCE_GIFT_CHANGE = "/api/v1/experience/gift/shop-owner/{experienceGiftId}"

    //일정관리
    const val ADMIN_MANAGING_RESERVATION="/api/v1/shop-owners/"

    //예약추가
    const val ADMIN_ADD_RESERVATION= "/api/v1/shop-owners/"

    //해당 날짜에 생성된 예약 조회
    const val ADMIN_GET_RESERVATION="/api/v1/shop-owners/date"

    //예약 확정
    const val ADMIN_POST_RESERVATION="/api/v1/shop-owners/confirm"

    //사장님 선물 불러오기
    const val ADMIN_GET_GIFT="/api/v1/experience/gift/shop-owner"
}

enum class RESPONSE_STATE {
    OKAY,
    FAIL
}