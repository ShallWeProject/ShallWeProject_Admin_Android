package com.shall_we.admin.reservation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.shall_we.admin.R
import com.shall_we.admin.databinding.FragmentReservationBinding
import com.shall_we.admin.reservation.data.ReservationData


class ReservationFragment : Fragment() {

    private var _binding: FragmentReservationBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReservationBinding.inflate(inflater, container, false)

        // Sample data for testing.
        val myDataset = listOf(
            ReservationData("[성수]인기 베이킹 클래스", "기념일케이크 사지말고 만들어요"),
            ReservationData("[홍대]인기 공예 클래스", "테마가 있는 프라이빗 칵테일 클래스")
            // Add more data here...
        )

        val viewManager = LinearLayoutManager(requireContext())
        val viewAdapter = ReservationAdapter(myDataset){ reservation ->
            // Handle item click here.
            navigateToOtherFragment(reservation)
        }

        binding.recyclerview.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }


        val view = binding.root
        return view
    }

    private fun navigateToOtherFragment(reservation : ReservationData){

        val newFragment = ManagingReservationFragment() // 전환할 다른 프래그먼트 객체 생성
        val bundle = Bundle()
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
}