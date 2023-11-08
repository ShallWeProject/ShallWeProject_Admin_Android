package com.shall_we.admin.product

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.shall_we.admin.R
import com.shall_we.admin.databinding.FragmentManagingProductBinding
import com.shall_we.admin.schedule.CustomAlertDialog


class ManagingProductFragment : Fragment() {
    private var _binding: FragmentManagingProductBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentManagingProductBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        //val scrollView = binding.scrollView

        //val list = resources.getStringArray(R.array.select_category).toList()
        val list = listOf("베이킹", "공예", "문화예술", "아웃도어", "스포츠")
        _binding!!.spinner.adapter =
            CategorySpinnnerAdapter(requireContext(), R.layout.dropdown_item, list)
        _binding!!.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val value = binding.spinner.getItemAtPosition(p2).toString()
                binding.category.text = value
//                Toast.makeText(requireContext(), value, Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                // 선택되지 않은 경우
            }
        }

        binding.btnSave.setOnClickListener {
            val newAlertDialog = CustomAlertDialog(requireContext(), R.layout.custom_popup_layout)
            newAlertDialog.setDialogContent("변경사항이 저장되었습니다.")
            val newDialog = newAlertDialog.create()

            val layoutParams = WindowManager.LayoutParams()
//        layoutParams.copyFrom(newAlertDialog.window?.attributes)
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

        binding.btnBack.setOnClickListener {
            val productFragment = ProductFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, productFragment)
                .addToBackStack(null)
                .commit()
            //navigateToOriginalFragment()
            //Toast.makeText(requireContext(), "눌렀음..", Toast.LENGTH_SHORT).show()
        }
        return binding.root
    }

    private fun navigateToOriginalFragment() {
        parentFragmentManager.popBackStack("fragment", 0) // 백 스택에서 이전 Fragment로 이동
    }
}

