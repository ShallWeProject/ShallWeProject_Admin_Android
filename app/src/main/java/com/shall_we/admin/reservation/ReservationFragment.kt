package com.shall_we.admin.reservation
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.shall_we.admin.R
import com.shall_we.admin.databinding.FragmentReservationBinding
import com.shall_we.admin.reservation.data.ReservationData
import com.shall_we.admin.reservation.retrofit.ReservationService
import com.shall_we.admin.retrofit.RESPONSE_STATE


class ReservationFragment : Fragment() {

    private var _binding: FragmentReservationBinding? = null
    private val binding get() = _binding!!

    private val viewAdapter by lazy {
        ReservationAdapter(mutableListOf(
            //ReservationData(123,"[성수] 인기 베이킹 클래스", "기념일 레터링 케이크" + "사지 말고 함께 만들어요")
        )) { experienceGiftId,subtitle,title ->
            navigateToOtherFragment(experienceGiftId,subtitle,title)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReservationBinding.inflate(inflater, container, false)
        val viewManager = LinearLayoutManager(requireContext())

        binding.recyclerview.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        ReservationService().getReservationGift { responseState, data ->
            if (responseState == RESPONSE_STATE.OKAY) {
                Log.d("data",data.toString())
                viewAdapter.updateData(data ?: mutableListOf())
            } else {
                // Handle error
            }
        }
    }

    private fun navigateToOtherFragment(experienceGiftId: Long, title: String, subtitle: String) {
        val newFragment = ManagingReservationFragment()
        val bundle = Bundle()
        bundle.putLong("experienceGiftId", experienceGiftId)
        bundle.putString("title", title)
        bundle.putString("subtitle", subtitle)
        newFragment.arguments = bundle

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
