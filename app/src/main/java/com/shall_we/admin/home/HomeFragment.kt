package com.shall_we.admin.home

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.shall_we.admin.App
import com.shall_we.admin.R
import com.shall_we.admin.databinding.FragmentHomeBinding
import com.shall_we.admin.home.retrofit.DeleteAccountService
import com.shall_we.admin.login.data.RefreshTokenReq
import com.shall_we.admin.home.retrofit.SignOutService
import com.shall_we.admin.product.ProductFragment
import com.shall_we.admin.reservation.ReservationFragment
import com.shall_we.admin.retrofit.RESPONSE_STATE
import com.shall_we.admin.schedule.ScheduleFragment

class HomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentHomeBinding.inflate(inflater,container,false)

        binding.btnProduct.setOnClickListener {
            val newFragment = ProductFragment() // 전환할 다른 프래그먼트 객체 생성
            val bundle = Bundle()
            newFragment.arguments = bundle

            // 프래그먼트 전환
            parentFragmentManager.beginTransaction()
                .add(R.id.fragmentContainerView, newFragment)
                .addToBackStack(null)
                .commit()


        }
        binding.btnSchedule.setOnClickListener {
            val newFragment = ScheduleFragment() // 전환할 다른 프래그먼트 객체 생성
            val bundle = Bundle()
            newFragment.arguments = bundle

            // 프래그먼트 전환
            parentFragmentManager.beginTransaction()
                .add(R.id.fragmentContainerView, newFragment)
                .addToBackStack(null)
                .commit()


        }
        binding.btnReservation.setOnClickListener {
            val newFragment = ReservationFragment() // 전환할 다른 프래그먼트 객체 생성
            val bundle = Bundle()
            newFragment.arguments = bundle

            // 프래그먼트 전환
            parentFragmentManager.beginTransaction()
                .add(R.id.fragmentContainerView, newFragment)
                .addToBackStack(null)
                .commit()


        }

        binding.btnLogout.setOnClickListener {
            logoutDialog(it)
        }

        binding.btnDelete.setOnClickListener {
            deleteDialog(it)
        }
        return binding.root


    }

    private fun logoutRetrofitCall() {
        val refreshTokenArray = RefreshTokenReq(App.refreshToken!!)
        SignOutService().postSignOut(
            refreshTokenArray,
            completion = { responseState, responseBody ->
                when (responseState) {
                    RESPONSE_STATE.OKAY -> {
                        Log.d("retrofit", "logout api : ${responseBody}")
                        // 토큰 리셋
                        val sharedPref =
                            context?.getSharedPreferences("MY_APP_PREFS", Context.MODE_PRIVATE)
                        sharedPref?.edit()?.putString("ACCESS_TOKEN", null)?.apply()
                        sharedPref?.edit()?.putString("REFRESH_TOKEN", null)?.apply()
                        sharedPref?.edit()?.putString("PHONE_NUMBER", null)?.apply()
                        sharedPref?.edit()?.putString("PASSWORD", null)?.apply()

                        App.accessToken = sharedPref?.getString("ACCESS_TOKEN", null)
                        App.refreshToken = sharedPref?.getString("REFRESH_TOKEN", null)
                        App.phoneNumber = sharedPref?.getString("PHONE_NUMBER", null)
                        App.password = sharedPref?.getString("PASSWORD", null)

                        //로그인 화면으로 돌아가기
                        requireActivity().finish()
                    }

                    RESPONSE_STATE.FAIL -> {
                        Log.d("retrofit", "api 호출 에러")
                    }
                }
            })
    }

    private fun logoutDialog(view: View) {
        val myLayout = LayoutInflater.from(context).inflate(R.layout.dialog_logout, null)
        val build = AlertDialog.Builder(view.context).apply {
            setView(myLayout)
        }
        val dialog = build.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.show()

        myLayout.findViewById<Button>(R.id.btn_logout_confirm).setOnClickListener {
            // 로그아웃 api 연결
            logoutRetrofitCall()
            dialog.dismiss()

        }
        myLayout.findViewById<Button>(R.id.btn_cancel_logout).setOnClickListener {
            dialog.dismiss()
        }
    }
    private fun deleteAccountRetrofitCall() {
        DeleteAccountService().patchShopOwner(
            completion = { responseState, responseBody ->
                when (responseState) {
                    RESPONSE_STATE.OKAY -> {
                        Log.d("retrofit", "delete api : ${responseBody}")
                        // 토큰 리셋
                        val sharedPref =
                            context?.getSharedPreferences("MY_APP_PREFS", Context.MODE_PRIVATE)
                        sharedPref?.edit()?.putString("ACCESS_TOKEN", null)?.apply()
                        sharedPref?.edit()?.putString("REFRESH_TOKEN", null)?.apply()
                        sharedPref?.edit()?.putString("PHONE_NUMBER", null)?.apply()
                        sharedPref?.edit()?.putString("PASSWORD", null)?.apply()

                        App.accessToken = sharedPref?.getString("ACCESS_TOKEN", null)
                        App.refreshToken = sharedPref?.getString("REFRESH_TOKEN", null)
                        App.phoneNumber = sharedPref?.getString("PHONE_NUMBER", null)
                        App.password = sharedPref?.getString("PASSWORD", null)

                    }

                    RESPONSE_STATE.FAIL -> {
                        Log.d("retrofit", "api 호출 에러")
                    }
                }
            })
    }

    private fun deleteDialog(view: View) {
        val myLayout = LayoutInflater.from(context).inflate(R.layout.dialog_delete_account, null)
        val build = AlertDialog.Builder(view.context).apply {
            setView(myLayout)
        }
        val dialog = build.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.show()

        myLayout.findViewById<Button>(R.id.btn_delete_account_confirm).setOnClickListener {
            // 로그아웃 api 연결
            deleteAccountRetrofitCall()
            dialog.dismiss()

            //로그인 화면으로 돌아가기
            requireActivity().finish()

        }
        myLayout.findViewById<Button>(R.id.btn_cancel_delete).setOnClickListener {
            dialog.dismiss()
        }
    }
}