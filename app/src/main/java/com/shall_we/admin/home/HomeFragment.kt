package com.shall_we.admin.home

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.shall_we.admin.App
import com.shall_we.admin.R
import com.shall_we.admin.databinding.FragmentHomeBinding
import com.shall_we.admin.home.retrofit.DeleteAccountService
import com.shall_we.admin.home.retrofit.ReservationInfoService
import com.shall_we.admin.login.data.RefreshTokenReq
import com.shall_we.admin.home.retrofit.SignOutService
import com.shall_we.admin.login.LoginActivity
import com.shall_we.admin.product.ProductFragment
import com.shall_we.admin.reservation.ReservationFragment
import com.shall_we.admin.retrofit.RESPONSE_STATE
import com.shall_we.admin.schedule.ScheduleFragment
import java.text.SimpleDateFormat
import java.util.Calendar

class HomeFragment : Fragment() {

    var totalReservationCount = ""
    var reservCheckCount = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentHomeBinding.inflate(inflater,container,false)
        binding.date.text = getCurrentDate()
        reservInfoRetrofitCall( binding.totalReservCount,binding.reservCheckCount)

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
                        val sharedPref = context?.getSharedPreferences("com.shall_we.admin", Context.MODE_PRIVATE)

                        sharedPref?.edit()?.putString("access_token", null)?.apply()
                        sharedPref?.edit()?.putString("refresh_token", null)?.apply()
                        sharedPref?.edit()?.putString("phone_number", null)?.apply()
                        sharedPref?.edit()?.putString("password", null)?.apply()

                        App.accessToken = sharedPref?.getString("access_token", null)
                        App.refreshToken = sharedPref?.getString("refresh_token", null)
                        App.phoneNumber = sharedPref?.getString("phone_number", null)
                        App.password = sharedPref?.getString("password", null)

                        //로그인 화면으로 돌아가기
                        val intent = Intent(requireContext(), LoginActivity::class.java)
                        startActivity(intent)
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
                            context?.getSharedPreferences("com.shall_we.admin", Context.MODE_PRIVATE)
                        sharedPref?.edit()?.putString("access_token", null)?.apply()
                        sharedPref?.edit()?.putString("refresh_token", null)?.apply()
                        sharedPref?.edit()?.putString("phone_number", null)?.apply()
                        sharedPref?.edit()?.putString("password", null)?.apply()

                        App.accessToken = sharedPref?.getString("access_token", null)
                        App.refreshToken = sharedPref?.getString("refresh_token", null)
                        App.phoneNumber = sharedPref?.getString("phone_number", null)
                        App.password = sharedPref?.getString("password", null)

                        //로그인 화면으로 돌아가기
                        val intent = Intent(requireContext(), LoginActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
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
            // 회원탈퇴 api 연결
            deleteAccountRetrofitCall()
            dialog.dismiss()
        }
        myLayout.findViewById<Button>(R.id.btn_cancel_delete).setOnClickListener {
            dialog.dismiss()
        }
    }

    fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy.MM.dd") // 출력할 날짜 형식 지정
        return dateFormat.format(calendar.time)
    }

    private fun reservInfoRetrofitCall(totalCount : TextView, checkCount : TextView) {
        ReservationInfoService().reservationInfo(
            completion = { responseState, responseBody ->
                when (responseState) {
                    RESPONSE_STATE.OKAY -> {
                        if (responseBody != null) {
                            totalReservationCount = responseBody.data.bookedReservationsCount.toString()
                            reservCheckCount =  responseBody.data.bookedCheckCount.toString()
                            totalCount.text = totalReservationCount
                            checkCount.text = reservCheckCount
                        }
                    }

                    RESPONSE_STATE.FAIL -> {
                        Log.d("retrofit", "api 호출 에러")
                    }
                }
            })
    }
}