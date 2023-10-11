package com.shall_we.admin.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.shall_we.admin.R
import com.shall_we.admin.databinding.FragmentManagingProductBinding


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

        //val list = resources.getStringArray(R.array.select_category).toList()
        val list = listOf("베이킹", "공예", "문화예술","아웃도어", "스포츠")
        _binding!!.category.adapter = CategorySpinnnerAdapter(requireContext(), R.layout.dropdown_item, list)
        _binding!!.category.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val value = binding.category.getItemAtPosition(p2).toString()
                Toast.makeText(requireContext(), value, Toast.LENGTH_SHORT).show()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                // 선택되지 않은 경우
            }
        }
        return inflater.inflate(R.layout.fragment_managing_product, container, false)
    }
}