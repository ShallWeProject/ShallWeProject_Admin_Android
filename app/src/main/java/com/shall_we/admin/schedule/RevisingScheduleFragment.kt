package com.shall_we.admin.schedule

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.shall_we.admin.R
import com.shall_we.admin.databinding.FragmentRevisingScheduleBinding
import com.shall_we.admin.product.ProductFragment
import java.text.ParseException
import java.text.SimpleDateFormat

import java.util.Date
import java.util.Locale


class RevisingScheduleFragment : Fragment() {

    private var _binding: FragmentRevisingScheduleBinding? = null
    private val binding get() = _binding!!
    private lateinit var calendarView: MaterialCalendarView
    private val locale: Locale = Locale("ko")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRevisingScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        calendarView = binding.calendar


        val btnbtn = binding.btnbtn

        // 달력 날짜 선택 리스너를 설정합니다.
        calendarView.setOnDateChangedListener { widget, date, selected ->

            if (selected) {
                btnbtn.visibility = View.VISIBLE
            } else {
                btnbtn.visibility = View.INVISIBLE
            }
        }

        // btnbtn 클릭 이벤트 처리
        btnbtn.setOnClickListener {
            // 클릭 시 버튼 디자인을 변경합니다.
            // 선택된 상태에 따라 리소스 변경을 할 수 있습니다.
            if (btnbtn.isSelected) {
                // 선택 해제된 경우
                btnbtn.isSelected = false
            } else {
                // 선택된 경우
                btnbtn.isSelected = true
            }
        }

        binding.btnbtnbtn.setOnClickListener {
            val alertDialog = CustomAlertDialog(requireContext(),R.layout.custom_popup_layout)
            val dialog = alertDialog.create()

            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog.window?.attributes)
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
            layoutParams.gravity = Gravity.BOTTOM // 중앙으로 정렬
            dialog.window?.attributes = layoutParams
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 이 부분을 수정
            alertDialog.setNegativeButton("확인") { v ->
                // 부정 버튼을 클릭했을 때 실행되는 코드 작성
                // 예를 들어, 팝업 다이얼로그를 닫을 수 있습니다.
                dialog.dismiss()
            }
            dialog.show()
        }

        binding.btn4.setOnClickListener {
            val newFragment =  ProductFragment()// 전환할 다른 프래그먼트 객체 생성
            val bundle = Bundle()
            newFragment.arguments = bundle

            // 프래그먼트 전환
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, newFragment)
                .addToBackStack(null)
                .commit()


        }



    }


        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null  // to avoid memory leaks
        }

}
