package com.shall_we.admin.product

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.LayoutRes
import com.shall_we.admin.databinding.DropdownItemBinding
import com.shall_we.admin.databinding.FragmentManagingProductBinding

class CategorySpinnnerAdapter (context: Context, @LayoutRes private val resId: Int, private val categoryList: List<String>)
    : ArrayAdapter<String>(context, resId, categoryList) {

    // 드롭다운하지 않은 상태의 Spinner 항목의 뷰
    override fun getView(position: Int, converView: View?, parent: ViewGroup): View {
        val binding = DropdownItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.tvSpinner.text = categoryList[position]

        return binding.root
    }

    // 드롭다운된 항목들 리스트의 뷰
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = DropdownItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.tvSpinner.text = categoryList[position]

        return binding.root
    }

    override fun getCount() = categoryList.size
}
