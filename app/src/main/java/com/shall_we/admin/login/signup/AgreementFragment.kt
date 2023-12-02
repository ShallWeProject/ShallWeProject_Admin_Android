package com.shall_we.admin.login.signup

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.shall_we.admin.App.Companion.context
import com.shall_we.admin.R
import com.shall_we.admin.databinding.FragmentAgreementBinding
import com.shall_we.admin.login.LoginFragment
import com.shall_we.admin.login.data.MessageRes
import com.shall_we.admin.login.data.SignUpReq
import com.shall_we.admin.login.retrofit.IAuthSignUp
import com.shall_we.admin.login.retrofit.SignUpService
import com.shall_we.admin.login.retrofit.ValidCodeService
import com.shall_we.admin.retrofit.RESPONSE_STATE

class AgreementFragment : Fragment(){
    private lateinit var binding : FragmentAgreementBinding

    private lateinit var name : String
    private lateinit var phone : String
    private lateinit var password : String

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
        binding.tvAgree2More.setOnClickListener {
            agreementDialog(it, 1)
        }

        binding.tvAgree3More.setOnClickListener {
            agreementDialog(it, 2)
        }
        binding.btnNextAgree.setOnClickListener {
            val auth = SignUpReq(phoneNumber = phone, name = name, password = password, marketingConsent = binding.cbAgree4.isChecked)

            SignUpService().postAuthSignUp(auth, completion = {
                    responseState, responseBody ->
                when(responseState){
                    RESPONSE_STATE.OKAY -> {
                        Log.d("retrofit", "category api : ${responseBody.hashCode()}")
                        if(responseBody.hashCode() == 200){
                            requireActivity().supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE) // 현재 스택에 있는 모든 프래그먼트를 제거합니다.

                            val transaction = requireActivity().supportFragmentManager.beginTransaction()
                            transaction.replace(R.id.fragmentContainerView3, SignupSuccessFragment())
                            transaction.addToBackStack(null) // 이전 상태를 백 스택에 추가합니다.
                            transaction.commit()
                        }
                        else if (responseBody == 400){
                            Toast.makeText(context,"${responseBody}. 다시 시도해 주세요",Toast.LENGTH_LONG).show()

                        }
                    }
                    RESPONSE_STATE.FAIL -> {
                        Log.d("retrofit", "api 호출 에러")
                    }
                }
            })
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
        name = arguments?.getString("name","").toString()
        password = arguments?.getString("password","").toString()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAgreement()
    }

}