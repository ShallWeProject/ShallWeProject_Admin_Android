package com.shall_we.admin.login.signup

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ScrollView
import android.widget.TextView
import com.shall_we.admin.R
import com.shall_we.admin.databinding.FragmentAgreementBinding

class AgreementFragment : Fragment() {
    private lateinit var binding : FragmentAgreementBinding

    private lateinit var phone : String

    fun initAgreement() {

        val checkList = listOf(
            binding.cbAgree1,
            binding.cbAgree2,
            binding.cbAgree3
        )

        fun checkAllChecked(): Boolean {
            if (checkList.all { checkBox -> checkBox.isChecked }){
                binding.btnNextAgree.setBackgroundResource(R.drawable.btn_next)
                return true
            }
            binding.btnNextAgree.setBackgroundResource(R.drawable.btn_next_black)
            return false
        }

        binding.cbAgreeAll.setOnCheckedChangeListener { button, ischecked ->
            if (ischecked) {
                checkList.forEach { checkBox -> checkBox.isChecked = true }
                binding.cbAgree4.isChecked = true
            }
            else {
                checkList.forEach { checkBox -> checkBox.isChecked = false }
                binding.cbAgree4.isChecked = false
            }
        }

        binding.cbAgree1.setOnCheckedChangeListener { button, ischecked ->
            if (ischecked && checkAllChecked()) {
                binding.btnNextAgree.setBackgroundResource(R.drawable.btn_next)
                binding.btnNextAgree.isClickable = true
            }
            else {
                binding.btnNextAgree.setBackgroundResource(R.drawable.btn_next_black)
                binding.btnNextAgree.isClickable = false
            }
        }
        binding.cbAgree2.setOnCheckedChangeListener { button, ischecked ->
            if (ischecked && checkAllChecked()) {
                binding.btnNextAgree.setBackgroundResource(R.drawable.btn_next)
                binding.btnNextAgree.isClickable = true
            }
            else {
                binding.btnNextAgree.setBackgroundResource(R.drawable.btn_next_black)
                binding.btnNextAgree.isClickable = false
            }
        }
        binding.cbAgree3.setOnCheckedChangeListener { button, ischecked ->
            if (ischecked && checkAllChecked()) {
                binding.btnNextAgree.setBackgroundResource(R.drawable.btn_next)
                binding.btnNextAgree.isClickable = true
            }
            else {
                binding.btnNextAgree.setBackgroundResource(R.drawable.btn_next_black)
                binding.btnNextAgree.isClickable = false
            }
        }
        binding.cbAgree4.setOnCheckedChangeListener { button, ischecked ->
            // Todo: 마케팅동의 정보 전달.
        }
        binding.tvAgree2More.setOnClickListener {
            agreementDialog(it, 1)
        }

        binding.tvAgree3More.setOnClickListener {
            agreementDialog(it, 2)
        }
        binding.btnNextAgree.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("phone", phone)
            bundle.putBoolean("isChecked", binding.cbAgree4.isChecked)
            val newFragment = SignupSuccessFragment() // 전환할 다른 프래그먼트 객체 생성
            newFragment.arguments = bundle

            // 프래그먼트 전환
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView3, newFragment)
                .addToBackStack(null)
                .commit()

        }

    }

    @SuppressLint("MissingInflatedId")
    private fun agreementDialog(view: View, num: Int) {
        if (num == 1) {
            val myLayout =
                LayoutInflater.from(context).inflate(R.layout.dialog_agreement_more, null)
            val build = AlertDialog.Builder(view.context).apply {
                setView(myLayout)
            }
            val dialog = build.create()
            val textView =
                myLayout.findViewById<TextView>(R.id.tv_agreement_description) // 여기서 textViewId는 실제 TextView의 ID여야 합니다
//            textView.text = resources.getString(R.string.agreement_service)
            textView.movementMethod = ScrollingMovementMethod.getInstance()

            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()

            myLayout.findViewById<Button>(R.id.btn_close).setOnClickListener {
                dialog.dismiss()
            }
        }
        else if (num == 2) {
            val myLayout =
                LayoutInflater.from(context).inflate(R.layout.dialog_agreement_more2, null)
            val build = AlertDialog.Builder(view.context).apply {
                setView(myLayout)
            }
            val dialog = build.create()
            val myView =
                myLayout.findViewById<ScrollView>(R.id.my_view) // 여기서 textViewId는 실제 TextView의 ID여야 합니다
//            myView.movementMethod = ScrollingMovementMethod.getInstance()

            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()

            myLayout.findViewById<Button>(R.id.btn_close).setOnClickListener {
                dialog.dismiss()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAgreementBinding.inflate(inflater, container, false)

        phone = arguments?.getString("phone", "").toString()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAgreement()
    }


}