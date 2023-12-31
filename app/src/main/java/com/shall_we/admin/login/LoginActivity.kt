package com.shall_we.admin.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.shall_we.admin.App
import com.shall_we.admin.MainActivity
import com.shall_we.admin.R
import com.shall_we.admin.databinding.ActivityLoginBinding
import com.shall_we.admin.login.data.AuthRes
import com.shall_we.admin.login.data.SignInReq
import com.shall_we.admin.login.retrofit.IAuthSignIn
import com.shall_we.admin.login.retrofit.SignInService

class LoginActivity : AppCompatActivity() , IAuthSignIn {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkAutoLogin()

    }

    private fun checkAutoLogin() {
        val sharedPref = this.getSharedPreferences("MY_APP_PREFS", Context.MODE_PRIVATE)

        val phoneNumber = sharedPref.getString("PHONE_NUMBER", null)
        val password = sharedPref.getString("PASSWORD", null)

        if (phoneNumber != null && password != null) {
            val auth = SignInReq(phoneNumber,password)
            SignInService(this).postAuthSignIn(auth)
        }
    }

    override fun onPostAuthSignInSuccess(response: AuthRes) {
        setUserData(response)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onPostAuthSignInFailed(message: String) {
        val newFragment = LoginFragment() // 전환할 다른 프래그먼트 객체 생성
        val bundle = Bundle()
        newFragment.arguments = bundle

        // 프래그먼트 전환
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView3, newFragment)
            .addToBackStack(null)
            .commit()
    }
    private fun setUserData(response: AuthRes){
        val sharedPref = this.getSharedPreferences("MY_APP_PREFS", Context.MODE_PRIVATE)
        val accessToken = response.data.accessToken
        sharedPref?.edit()?.putString("ACCESS_TOKEN", accessToken)?.apply()
        val refreshToken = response.data.refreshToken
        sharedPref?.edit()?.putString("REFRESH_TOKEN", refreshToken)?.apply()

        App.accessToken = sharedPref?.getString("ACCESS_TOKEN", null)
        App.refreshToken = sharedPref?.getString("REFRESH_TOKEN", null)


        Log.d("login","access token ${App.accessToken}")
        Log.d("login", "refresh token ${App.refreshToken}")

    }

}