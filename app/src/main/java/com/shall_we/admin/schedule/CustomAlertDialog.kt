package com.shall_we.admin.schedule

import com.shall_we.admin.R
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import com.shall_we.admin.databinding.CustomPopupLayoutBinding


class CustomAlertDialog(private val context: Context) {

    private val binding: CustomPopupLayoutBinding= DataBindingUtil.inflate(
        LayoutInflater.from(context),

        R.layout.custom_popup_layout, // 레이아웃 파일명을 사용자의 실제 레이아웃에 맞게 수정
        null,
        false
    )

    fun create(): Dialog {
        val dialog = Dialog(context)
        dialog.setContentView(binding.root)
        return dialog
    }



    fun setNegativeButton(text: String, listener: View.OnClickListener) {
        binding.btnDialogNegative.text = text
        binding.btnDialogNegative.setOnClickListener(listener)
    }
}