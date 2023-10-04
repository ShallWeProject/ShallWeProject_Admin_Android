package com.shall_we.admin.schedule

import com.shall_we.admin.R
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.shall_we.admin.databinding.CustomPopupLayoutBinding


class CustomAlertDialog(private val context: Context, private val layoutResId: Int) {

    private val binding: ViewDataBinding = DataBindingUtil.inflate(
        LayoutInflater.from(context),
        layoutResId, // 사용자가 원하는 레이아웃 파일의 리소스 ID로 변경
        null,
        false
    )

    fun create(): Dialog {
        val dialog = Dialog(context)
        dialog.setContentView(binding.root)
        return dialog
    }
    fun setDialogContent(text: String) {
        // 사용자가 원하는 텍스트뷰 ID에 맞게 수정
        val textView = binding.root.findViewById<TextView>(R.id.tvDialogMessage)
        textView.text = text
    }


    fun setNegativeButton(text: String, listener: View.OnClickListener) {
        // 사용자가 원하는 버튼 ID에 맞게 수정
        val negativeButton = binding.root.findViewById<Button>(R.id.btnDialogNegative)
        negativeButton.text = text
        negativeButton.setOnClickListener(listener)
    }
    fun setPositiveButton(text: String, listener: View.OnClickListener) {
        // 사용자가 원하는 버튼 ID에 맞게 수정
        val positiveButton = binding.root.findViewById<Button>(R.id.btnDialogPositive)
        positiveButton.text = text
        positiveButton.setOnClickListener(listener)
    }
}
