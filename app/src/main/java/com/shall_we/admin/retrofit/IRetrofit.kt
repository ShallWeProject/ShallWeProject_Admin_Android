package com.shall_we.admin.retrofit

import com.google.gson.JsonElement
import com.shall_we.admin.home.data.ReservationInfoRes
import com.shall_we.admin.login.data.AuthRes
import com.shall_we.admin.login.data.ChangePasswordReq
import com.shall_we.admin.login.data.MessageRes
import com.shall_we.admin.login.data.RefreshTokenReq
import com.shall_we.admin.login.data.SendOneReq
import com.shall_we.admin.login.data.SignInReq
import com.shall_we.admin.login.data.SignUpReq
import com.shall_we.admin.login.data.ValidCodeReq
import com.shall_we.admin.product.data.AdminExperienceReq
import com.shall_we.admin.product.data.AdminMainRes
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface IRetrofit {

    // 사장 로그인
    @POST(API.AUTH_SIGN_IN)
    fun authSignIn(@Body auth: SignInReq): Call<AuthRes>

    // 사장 회원가입
    @POST(API.AUTH_SIGN_UP)
    fun authSignUp(@Body auth: SignUpReq): Call<AuthRes>

    // 사장 로그아웃
    @POST(API.AUTH_SIGN_OUT)
    fun authSignOut(@Body refreshTokenArray : RefreshTokenReq): Call<MessageRes>

    // 사장 탈퇴
    @PATCH(API.PATCH_SHOP_OWNER)
    fun patchShopOwners() : Call<MessageRes>

    // 토큰 리프레시
    @POST(API.AUTH_REFRESH)
    fun tokenRefresh(@Body refreshTokenArray : RefreshTokenReq): Call<AuthRes>

    // 번호 인증 - 문자 보내기
    @POST(API.SEND_ONE)
    fun sendOne(@Body phoneNumber : SendOneReq): Call <JsonElement>

    // 번호 인증 - 인증번호 확인
    @POST(API.VALID_VERIFICATION)
    fun validVerification(@Body validVerificationArray : ValidCodeReq): Call<JsonElement>

    @GET(API.ADMIN_EXPERIENCE_GIFT)
    fun adminExperienceGift(): Call<JsonElement>

    @GET(API.ADMIN_EXPERIENCE_GIFT_MAIN)
    fun adminExperienceGiftMain(): Call<AdminMainRes>

    @POST(API.ADMIN_EXPERIENCE_GIFT_REGISTER)
    fun postAdminExperienceGiftRegister(@Body adminExperienceReq: AdminExperienceReq): Call<JsonElement>


    @PUT(API.ADMIN_EXPERIENCE_GIFT_DELETE)
    fun putAdminExperienceGift(@Path("experienceGiftId") experienceGiftId: Int, @Body adminExperienceReqArray : AdminExperienceReq): Call<JsonElement>


    @DELETE(API.ADMIN_EXPERIENCE_GIFT_DELETE)
    fun delAdminExperienceGift(@Path("experienceGiftId") experienceGiftId: Int): Call<JsonElement>

    // 비밀번호 변경
    @PATCH(API.CHANGE_PASSWORD)
    fun changePassword(@Body changePassword : ChangePasswordReq) : Call<JsonElement>

    // 메인 페이지 예약 정보
    @GET(API.HOME_RESERVATION_INFO)
    fun homeReservationInfo() : Call<ReservationInfoRes>

}

