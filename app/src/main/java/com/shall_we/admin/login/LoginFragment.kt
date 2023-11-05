package com.shall_we.admin.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.shall_we.admin.MainActivity
import com.shall_we.admin.R
import com.shall_we.admin.databinding.FragmentLoginBinding
import com.shall_we.admin.login.signin.ResetPasswordFragment
import com.shall_we.admin.login.signup.PhoneAuthFragment
import com.shall_we.admin.product.ProductFragment

class LoginFragment : Fragment() {

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
            val newFragment = ResetPasswordFragment() // 전환할 다른 프래그먼트 객체 생성
            val bundle = Bundle()
            newFragment.arguments = bundle

            // 프래그먼트 전환
            parentFragmentManager.beginTransaction()
                .add(R.id.fragmentContainerView3, newFragment)
                .addToBackStack(null)
                .commit()
        }

        binding.btnLoginhome.setOnClickListener {
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }


}
