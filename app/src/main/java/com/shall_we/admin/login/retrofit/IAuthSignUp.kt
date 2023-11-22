package com.shall_we.admin.login.retrofit

import com.shall_we.admin.login.data.MessageRes


interface IAuthSignUp {
    fun onPostAuthSignUpSuccess(response: MessageRes)
    fun onPostAuthSignUpFailed(message: String)
}