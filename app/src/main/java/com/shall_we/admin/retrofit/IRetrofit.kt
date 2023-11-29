package com.shall_we.admin.retrofit

import com.google.gson.JsonElement
import com.shall_we.admin.login.data.AuthRes
import com.shall_we.admin.login.data.ChangePasswordReq
import com.shall_we.admin.login.data.MessageRes
import com.shall_we.admin.login.data.RefreshTokenReq
import com.shall_we.admin.login.data.SendOneReq
import com.shall_we.admin.login.data.SendOneRes
import com.shall_we.admin.login.data.SignInReq
import com.shall_we.admin.login.data.SignUpReq
import com.shall_we.admin.login.data.ValidCodeReq
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

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

    // 비밀번호 변경
    @PATCH(API.CHANGE_PASSWORD)
    fun changePassword(@Body changePassword : ChangePasswordReq) : Call<JsonElement>

    // 메인 페이지 예약 정보
    @GET(API.HOME_RESERVATION_INFO)
    fun homeReservationInfo() : Call<JsonElement>

}

