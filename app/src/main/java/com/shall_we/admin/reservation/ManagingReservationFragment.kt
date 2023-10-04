package com.shall_we.admin.reservation

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.isNotEmpty
import androidx.recyclerview.widget.LinearLayoutManager
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.shall_we.admin.R
import com.shall_we.admin.databinding.FragmentManagingReservationBinding
import com.shall_we.admin.databinding.FragmentRevisingScheduleBinding
import com.shall_we.admin.schedule.CustomAlertDialog
import java.util.Locale


class ManagingReservationFragment : Fragment() {
    private var _binding: FragmentManagingReservationBinding? = null
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
        // Inflate the layout for this fragment
        _binding = FragmentManagingReservationBinding.inflate(inflater, container, false)



        calendarView = binding.calendar
        val recyclerView = binding.recyclerview
        recyclerView.layoutManager = LinearLayoutManager(requireContext()) // 레이아웃 매니저 설정

        // 어댑터를 생성하고 RecyclerView에 설정
        val reservationList = mutableListOf<ReservationListData>() // 예약 데이터를 적절히 초기화해야 함

        reservationList.add(ReservationListData("이수진 외 1명", "10:00 AM", "123-456-7890"))
        reservationList.add(ReservationListData("김예린 외 3명", "11:00 AM", "987-654-3210"))
        reservationList.add(ReservationListData("박준영 외 1명", "9:00 AM", "987-654-3210"))
        reservationList.add(ReservationListData("이수진 외 1명", "10:00 AM", "123-456-7890"))
        reservationList.add(ReservationListData("김예린 외 3명", "11:00 AM", "987-654-3210"))
        reservationList.add(ReservationListData("박준영 외 1명", "9:00 AM", "987-654-3210"))
        reservationList.add(ReservationListData("이수진 외 1명", "10:00 AM", "123-456-7890"))
        reservationList.add(ReservationListData("김예린 외 3명", "11:00 AM", "987-654-3210"))
        reservationList.add(ReservationListData("박준영 외 1명", "9:00 AM", "987-654-3210"))


        val adapter = ReservationListAdapter(reservationList) { reservation ->
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
            }
            dialog.show()

        }
        recyclerView.adapter = adapter
//        calendarView.setOnDateChangedListener { widget, date, selected ->
//            // 예약 데이터 리스트에서 선택된 날짜와 일치하는 항목만 필터링합니다.
//
//
//            if (recyclerView.isNotEmpty()) {
//                // 필터링된 예약 리스트를 RecyclerView 어댑터에 설정하고,
//                recyclerView.adapter = ReservationListAdapter(filteredReservationList) { reservation ->
//                    // 아이템 클릭 이벤트 처리
//                    onItemClicked(reservation)
//                }
//
//                // RecyclerView를 보여줍니다.
//                recyclerView.visibility = View.VISIBLE
//            } else {
//                // 해당 날짜에 예약이 없다면 RecyclerView를 숨깁니다.
//                recyclerView.visibility = View.GONE
//            }
//        }



        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        calendarView = binding.calendar





    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // to avoid memory leaks
    }

}




