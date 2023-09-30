package com.shall_we.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shall_we.admin.databinding.FragmentHomeBinding
import com.shall_we.admin.product.ProductFragment
import com.shall_we.admin.reservation.ReservationFragment
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
                .replace(R.id.fragmentContainerView, newFragment)
                .addToBackStack(null)
                .commit()


        }
        binding.btnSchedule.setOnClickListener {
            val newFragment = ScheduleFragment() // 전환할 다른 프래그먼트 객체 생성
            val bundle = Bundle()
            newFragment.arguments = bundle

            // 프래그먼트 전환
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, newFragment)
                .addToBackStack(null)
                .commit()


        }
        binding.btnReservation.setOnClickListener {
            val newFragment = ReservationFragment() // 전환할 다른 프래그먼트 객체 생성
            val bundle = Bundle()
            newFragment.arguments = bundle

            // 프래그먼트 전환
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, newFragment)
                .addToBackStack(null)
                .commit()


        }
        return binding.root


    }

}