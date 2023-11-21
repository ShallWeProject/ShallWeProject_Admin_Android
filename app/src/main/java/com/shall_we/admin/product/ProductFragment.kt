package com.shall_we.admin.product

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.shall_we.admin.R
import com.shall_we.admin.databinding.FragmentProductBinding
import com.shall_we.admin.reservation.ReservationListAdapter
import com.shall_we.admin.schedule.CustomAlertDialog


class ProductFragment : Fragment() {
    private var _binding: FragmentProductBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductBinding.inflate(inflater, container, false)

        // Sample data for testing.
        val myDataset = listOf(
            ProductData(1, "[성수]인기 베이킹 클래스", "기념일케이크 사지말고 만들어요"),
            ProductData(2, "[홍대]인기 공예 클래스", "테마가 있는 프라이빗 칵테일 클래스")
            // Add more data here...
        )

        val viewManager = LinearLayoutManager(requireContext())
        val viewAdapter = ProductAdapter(myDataset) { product ->
            navigateToEditFragment(product)
        }

        binding.rvProduct.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        binding.btnAdd.setOnClickListener {
            navigateToNewFragment()
        }


        binding.btnDelete.setOnClickListener {
            val alertDialog = CustomAlertDialog(requireContext(), R.layout.dialog_delete_product)
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

            alertDialog.setPositiveButton("삭제하기") { v ->

                dialog.dismiss()
                val newAlertDialog =
                    CustomAlertDialog(requireContext(), R.layout.custom_popup_layout)
                newAlertDialog.setDialogContent("해당 상품이 삭제되었습니다.")
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


        return binding.root
    }

    private fun navigateToEditFragment(product : ProductData) {
        val newFragment = ManagingProductFragment()
        val bundle = Bundle()
        newFragment.arguments = bundle
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, newFragment)
            .addToBackStack("fragment")
            .commit()

//        val fragment = parentFragmentManager.findFragmentByTag("fragment")
//        if (fragment != null) {
//            Toast.makeText(requireContext(), "있어..", Toast.LENGTH_SHORT).show()
//        } else {
//            Toast.makeText(requireContext(), "없어..", Toast.LENGTH_SHORT).show()
//        }
    }

    private fun navigateToNewFragment() {
        val newFragment = ManagingProductFragment()
        val bundle = Bundle()
        newFragment.arguments = bundle
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, newFragment)
            .addToBackStack("fragment")
            .commit()
    }

    override fun onResume() {
        super.onResume()
        val supportActionBar = (requireActivity() as AppCompatActivity).supportActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}