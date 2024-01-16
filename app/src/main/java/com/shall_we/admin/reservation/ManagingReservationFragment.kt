package com.shall_we.admin.reservation

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.shall_we.admin.R
import com.shall_we.admin.databinding.FragmentManagingReservationBinding
import com.shall_we.admin.reservation.data.ReservationListData
import com.shall_we.admin.reservation.retrofit.ReservationService
import com.shall_we.admin.reservation.viewmodel.ReservationViewModel
import com.shall_we.admin.retrofit.RESPONSE_STATE
import com.shall_we.admin.schedule.CustomAlertDialog
import java.util.Locale


class ManagingReservationFragment : Fragment() {
    private var _binding: FragmentManagingReservationBinding? = null
    private val binding get() = _binding!!
    private lateinit var calendarView: MaterialCalendarView
    private val locale: Locale = Locale("ko")
    private val reservationList = mutableListOf<ReservationListData>() // 예약 데이터 리스트
    private lateinit var viewModel: ReservationViewModel
    val giftId:Long=3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentManagingReservationBinding.inflate(inflater, container, false)


        calendarView = binding.calendar
        val recyclerView = binding.recyclerview
        recyclerView.layoutManager = LinearLayoutManager(requireContext()) // 레이아웃 매니저 설정

        // 어댑터를 생성하고 RecyclerView에 설정
        val reservationList = mutableListOf<ReservationListData>() // 예약 데이터를 적절히 초기화해야 함


        viewModel = ViewModelProvider(this).get(ReservationViewModel::class.java)

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        viewModel.reservations.observe(viewLifecycleOwner, Observer { reservations ->
            val adapter = ReservationListAdapter(reservations) { reservation ->
                val alertDialog = CustomAlertDialog(requireContext(),R.layout.custom_alert_dialog_layout)
                val dialog = alertDialog.create()

                val layoutParams = WindowManager.LayoutParams()
                layoutParams.copyFrom(dialog.window?.attributes)
                layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT
                layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
                layoutParams.gravity = Gravity.CENTER // 중앙으로 정렬
                dialog.window?.attributes = layoutParams
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 이 부분을 수정
                alertDialog.setNegativeButton("취소") { v ->

                    dialog.dismiss()
                }

                alertDialog.setPositiveButton("예약 확정"){v->
                    val reservationService = ReservationService()  // ReservationService 인스턴스 생성
                    val reservationId: Long = reservation.reservationId // 예약 ID 설정

                    reservationService.postReservation(reservationId) { state, messageRes ->
                        if (state == RESPONSE_STATE.OKAY) {
                            dialog.dismiss()
                            val newAlertDialog = CustomAlertDialog(requireContext(),R.layout.custom_popup_layout)
                            newAlertDialog.setDialogContent("해당 예약이 확정되었습니다.")
                            val newDialog = newAlertDialog.create()

                            val layoutParams = WindowManager.LayoutParams()
                            layoutParams.copyFrom(dialog.window?.attributes)
                            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
                            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
                            layoutParams.gravity = Gravity.BOTTOM // 중앙으로 정렬
                            newDialog.window?.attributes = layoutParams
                            newDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 이 부분을 수정

                            newAlertDialog.setNegativeButton("확인") { v ->
                                newDialog.dismiss()
                            }
                            newDialog.show()
                        } else {
                            // 오류 처리
                            Log.d("ReservationService", "Failed to confirm reservation.")
                        }
                    }
                }
                dialog.show()
            }
            binding.recyclerview.adapter = adapter
        })





        calendarView.setOnDateChangedListener { widget, date, selected ->
            if (selected) {
                // 선택된 날짜를 YYYY-MM-DD 형식의 문자열로 변환합니다.
                val date = String.format("%04d-%02d-%02d", date.year, date.month + 1, date.day)

                // API를 호출하는 함수를 실행합니다.
                viewModel.fetchReservations(giftId,date)
            }
        }


        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // to avoid memory leaks
    }




}




