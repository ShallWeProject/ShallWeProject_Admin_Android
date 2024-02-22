package com.shall_we.admin.schedule

import ScheduleService
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.shall_we.admin.R
import com.shall_we.admin.databinding.FragmentScheduleBinding
import com.shall_we.admin.retrofit.RESPONSE_STATE
import com.shall_we.admin.schedule.data.ScheduleData

class ScheduleFragment : Fragment() {

    private var _binding: FragmentScheduleBinding? = null
    private val binding get() = _binding!!

    private val viewAdapter by lazy {
        ScheduleAdapter(mutableListOf()) { experienceGiftId, subtitle, title ->
            navigateToOtherFragment(experienceGiftId, subtitle, title)
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScheduleBinding.inflate(inflater, container, false)
        val view = binding.root

        val viewManager = LinearLayoutManager(requireContext())

        val scheduleService = ScheduleService()
        scheduleService.getGift { responseState, scheduleList ->
            if (responseState == RESPONSE_STATE.OKAY && scheduleList != null) {
                viewAdapter.scheduleList = scheduleList
                viewAdapter.notifyDataSetChanged()
            } else {
                // 데이터를 가져오는 데 실패했을 때의 처리 코드를 여기에 작성합니다.
            }
        }

        binding.recyclerview.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        return view

    }

    private fun navigateToOtherFragment(experienceGiftId: Long, title: String, subtitle: String) {
        val newFragment = RevisingScheduleFragment() // 전환할 다른 프래그먼트 객체 생성
        val bundle = Bundle()
        bundle.putLong("experienceGiftId", experienceGiftId)
        bundle.putString("subtitle", subtitle)
        bundle.putString("title", title)
        newFragment.arguments = bundle

        // 프래그먼트 전환
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, newFragment)
            .addToBackStack(null)
            .commit()
    }


    override fun onResume() {
        super.onResume()
        val supportActionBar = (requireActivity() as AppCompatActivity).supportActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
