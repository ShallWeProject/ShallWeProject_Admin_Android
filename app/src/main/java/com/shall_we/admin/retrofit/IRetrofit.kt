package com.shall_we.admin.retrofit

import com.google.gson.JsonElement
import com.shall_we.admin.login.data.AuthRes
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

    //유저 로그인
    @POST(API.AUTH_SIGN_IN)
    fun authSignIn(@Body auth: SignInReq): Call<AuthRes>

    // 유저 회원가입
    @POST(API.AUTH_SIGN_UP)
    fun authSignUp(@Body auth: SignUpReq): Call<MessageRes>

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
}

