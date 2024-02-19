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
    private var experienceGiftId: Long? = null
    private var title: String? = null
    private var subtitle: String? = null
    private val locale: Locale = Locale("ko")
    private val reservationList = mutableListOf<ReservationListData>() // 예약 데이터 리스트
    private lateinit var viewModel: ReservationViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            experienceGiftId = it.getLong("experienceGiftId")
            title=it.getString("subtitle")
            subtitle=it.getString("title")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentManagingReservationBinding.inflate(inflater, container, false)

        subtitle?.let {
            binding.description.setText(it)
        }
        title?.let{
            binding.title.setText(it)
        }

        calendarView = binding.calendar
        val recyclerView = binding.recyclerview
        recyclerView.layoutManager = LinearLayoutManager(requireContext()) // 레이아웃 매니저 설정
        viewModel = ViewModelProvider(this).get(ReservationViewModel::class.java)
        calendarView.setOnDateChangedListener { widget, date, selected ->
            if (selected) {
                // 선택된 날짜를 YYYY-MM-DD 형식의 문자열로 변환합니다.
                val date = String.format("%04d-%02d-%02d", date.year, date.month + 1, date.day)

                // API를 호출하는 함수를 실행합니다.
                experienceGiftId?.let {
                    viewModel.fetchReservations(it, date).observe(viewLifecycleOwner, Observer { reservations ->
                        var adapter = binding.recyclerview.adapter as? ReservationListAdapter
                        if (adapter == null) {
                            adapter = ReservationListAdapter(reservations) { reservation ->
                                val alertDialog = CustomAlertDialog(requireContext(),R.layout.custom_alert_dialog_layout)
                                alertDialog.setReservations(reservations)
                                val dialog = alertDialog.create()
                                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                                Log.d("눌렸나","")
                                alertDialog.setNegativeButton("취소") { v ->
                                    dialog.dismiss()
                                }

                                alertDialog.setPositiveButton("예약 확정"){v->
                                    val reservationService = ReservationService()  // ReservationService 인스턴스 생성
                                    val reservationId: Long =reservation.reservationId // 예약 ID 설정

                                    reservationService.postReservation(reservationId) { state, messageRes ->
                                        if (state == RESPONSE_STATE.OKAY) {
                                            Log.d("ReservationService", "Reservation confirmed successfully.")
                                            dialog.dismiss()
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
                                                // Here, fetch the new data and update the adapter.
                                                experienceGiftId?.let {
                                                    viewModel.fetchReservations(it, date).observe(viewLifecycleOwner, Observer { newReservations ->
                                                        (binding.recyclerview.adapter as? ReservationListAdapter)?.reservationList = newReservations
                                                    })
                                                }
                                                dialog.dismiss()
                                            }
                                            dialog.show()

                                        } else {
                                            Log.d("ReservationService", "Failed to confirm reservation.")
                                        }
                                    }

                                }
                                dialog.show()
                            }
                            Log.d("AdapterCreation", "Adapter has been created")
                            binding.recyclerview.adapter = adapter
                        } else {
                            adapter.reservationList = reservations
                        }
                    })
                }


            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // to avoid memory leaks
    }

}




