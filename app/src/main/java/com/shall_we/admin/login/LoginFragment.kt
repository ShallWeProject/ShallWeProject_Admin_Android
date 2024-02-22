package com.shall_we.admin.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.shall_we.admin.App
import com.shall_we.admin.MainActivity
import com.shall_we.admin.R
import com.shall_we.admin.databinding.FragmentLoginBinding
import com.shall_we.admin.login.data.AuthRes
import com.shall_we.admin.login.data.SignInReq
import com.shall_we.admin.login.retrofit.IAuthSignIn
import com.shall_we.admin.login.retrofit.SignInService
import com.shall_we.admin.login.signin.ChangePasswordFragment
import com.shall_we.admin.login.signup.PhoneAuthFragment

class LoginFragment : Fragment(), IAuthSignIn {
    private lateinit var auth : SignInReq

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.btnLogin.setOnClickListener { // 로그인 버튼
            if(binding.etLoginPassword.text != null && binding.etLoginPhoneNumber.text != null){
                auth = SignInReq(binding.etLoginPhoneNumber.text.toString(),binding.etLoginPassword.text.toString())
                SignInService(this).postAuthSignIn(auth)
            }
        }
        binding.btnSignup.setOnClickListener {
            val newFragment = PhoneAuthFragment() // 전환할 다른 프래그먼트 객체 생성
            val bundle = Bundle()
            newFragment.arguments = bundle

            // 프래그먼트 전환
            parentFragmentManager.beginTransaction()
                .add(R.id.fragmentContainerView3, newFragment)
                .addToBackStack(null)
                .commit()
        }
        binding.btnResetPassword.setOnClickListener {
            val newFragment = ChangePasswordFragment() // 전환할 다른 프래그먼트 객체 생성
            val bundle = Bundle()
            newFragment.arguments = bundle

            // 프래그먼트 전환
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView3, newFragment)
                .addToBackStack(null)
                .commit()
        }

        return binding.root
    }

    override fun onPostAuthSignInSuccess(response: AuthRes) {
        setUserData(auth, response)
        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    override fun onPostAuthSignInFailed(message: String) {
        Log.d("login2","onPostAuthSignInFailed $message")
        val errorMessage1 = "유효하지 않는 전화번호입니다."
        val errorMessage2 = "비밀번호가 일치하지 않습니다."

        // 에러 메시지에 따라 다른 액션 수행
        if (message.contains(errorMessage1)) {
            Toast.makeText(context,errorMessage1,Toast.LENGTH_LONG).show()
        } else if (message.contains(errorMessage2)) {
            Toast.makeText(context,errorMessage2,Toast.LENGTH_LONG).show()

        } else {
            Toast.makeText(context,"알 수 없는 에러입니다.",Toast.LENGTH_LONG).show()

        }
    }

    private fun setUserData(auth : SignInReq, response: AuthRes){
        val sharedPref = context?.getSharedPreferences("com.shall_we.admin", Context.MODE_PRIVATE)
        val accessToken = response.data.accessToken
        sharedPref?.edit()?.putString("access_token", accessToken)?.apply()
        val refreshToken = response.data.refreshToken
        sharedPref?.edit()?.putString("refresh_token", refreshToken)?.apply()
        val phoneNumber = auth.phoneNumber
        sharedPref?.edit()?.putString("phone_number", phoneNumber)?.apply()
        val password = auth.password
        sharedPref?.edit()?.putString("password", password)?.apply()

        App.accessToken = sharedPref?.getString("access_token", null)
        App.refreshToken = sharedPref?.getString("refresh_token", null)
        App.phoneNumber = sharedPref?.getString("phone_number",null)
        App.password = sharedPref?.getString("password",null)
    }
}
