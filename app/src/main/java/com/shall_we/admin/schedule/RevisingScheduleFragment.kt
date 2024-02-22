package com.shall_we.admin.schedule

import ScheduleService
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.shall_we.admin.R
import com.shall_we.admin.databinding.FragmentRevisingScheduleBinding
import com.shall_we.admin.product.ProductFragment
import com.shall_we.admin.schedule.data.ReservationInfo
import com.shall_we.admin.schedule.data.TimeData

import java.util.Locale


class RevisingScheduleFragment : Fragment() {

    private var _binding: FragmentRevisingScheduleBinding? = null
    private val binding get() = _binding!!
    private lateinit var calendarView: MaterialCalendarView
    private var experienceGiftId: Long? = null
    private var title: String? = null
    private var subtitle: String? = null
    private val locale: Locale = Locale("ko")
    private val scheduleService = ScheduleService()  // ScheduleService의 인스턴스를 생성합니다.
    private var selectedTimes: List<String> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            experienceGiftId = it.getLong("experienceGiftId")
            title=it.getString("title")
            subtitle=it.getString("subtitle")
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

        subtitle?.let {
            binding.description.setText(it)
        }
        title?.let{
            binding.title.setText(it)
        }

        val rec = binding.rec

        // 리사이클러뷰에 어댑터를 설정합니다.
        val timeList = listOf<TimeData>(
            TimeData("1시"),
            TimeData("2시"),
            TimeData("3시"),
            TimeData("4시"),
            TimeData("5시"),
            TimeData("6시"),
            TimeData("6시"),
            TimeData("7시"),
            TimeData("8시"),
            TimeData("9시"),
        )  // 실제 데이터로 교체해야 합니다.

        val timeAdapter = SchduleTimeAdapter(timeList) { timeData ->
            // 아이템 클릭 시 수행할 행동을 정의합니다.
        }

        rec.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = timeAdapter
        }

        rec.visibility = View.INVISIBLE
        // 달력 날짜 선택 리스너를 설정합니다.
        // 달력 날짜 선택 리스너를 설정합니다.
        calendarView.setOnDateChangedListener { widget, date, selected ->
            if (selected) {
                rec.visibility = View.VISIBLE

                // 선택된 날짜를 문자열로 변환합니다.
                val selectedDate = String.format("%04d-%02d-%02d", date.year, date.month + 1, date.day)

                // 선택된 시간들을 리스트로 만듭니다.
                selectedTimes = timeList.map { it.toLocalTimeFormat() }  // TimeData의 toLocalTimeFormat 메서드를 사용하여 시간을 변환합니다.
            } else {
                rec.visibility = View.INVISIBLE
            }
        }

        binding.btnbtnbtn.setOnClickListener {
            // 선택된 날짜를 문자열로 변환합니다.
            // 월과 일이 한 자리 수일 때 앞에 0을 붙입니다.
            val selectedDate = String.format("%04d-%02d-%02d", calendarView.selectedDate.year, calendarView.selectedDate.month + 1, calendarView.selectedDate.day)

            // 예약 정보를 만듭니다.
            val reservationInfo = ReservationInfo(
               experienceGiftId,
                dateTimeMap = mapOf(
                    selectedDate to selectedTimes
                )

            )
            scheduleService.addReservation(reservationInfo) { state, result ->
                // 응답 처리 코드를 여기에 작성합니다.

            }


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
