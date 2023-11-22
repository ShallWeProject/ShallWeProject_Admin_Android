package com.shall_we.admin.login.retrofit

import com.shall_we.admin.login.data.AuthRes

interface IAuthSignIn {
    fun onPostAuthSignInSuccess(response: AuthRes)
    fun onPostAuthSignInFailed(message: String)
}