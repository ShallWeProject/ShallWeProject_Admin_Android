package com.shall_we.admin.retrofit

import com.google.gson.JsonElement
import com.shall_we.admin.login.data.AuthRes
import com.shall_we.admin.login.data.MessageRes
import com.shall_we.admin.login.data.RefreshTokenReq
import com.shall_we.admin.login.data.SendOneReq
import com.shall_we.admin.login.data.SendOneRes
import com.shall_we.admin.login.data.SignInReq
import com.shall_we.admin.login.data.SignUpReq
import com.shall_we.admin.login.data.ValidCodeReq
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.POST

interface IRetrofit {

    //유저 로그인
    @POST(API.AUTH_SIGN_IN)
    fun authSignIn(@Body auth: SignInReq): Call<AuthRes>

    // 유저 회원가입
    @POST(API.AUTH_SIGN_UP)
    fun authSignUp(@Body auth: SignUpReq): Call<AuthRes>

    // 유저 로그아웃
    @POST(API.AUTH_SIGN_OUT)
    fun authSignOut(@Body refreshTokenArray : RefreshTokenReq): Call<MessageRes>

    @PATCH(API.PATCH_SHOP_OWNER)
    fun patchShopOwners() : Call<MessageRes>

    @POST(API.AUTH_REFRESH)
    fun tokenRefresh(@Body refreshTokenArray : RefreshTokenReq): Call<AuthRes>

    @POST(API.SEND_ONE)
    fun sendOne(@Body phoneNumber : SendOneReq): Call <JsonElement>

    @POST(API.VALID_VERIFICATION)
    fun validVerification(@Body validVerificationArray : ValidCodeReq): Call<JsonElement>

}

