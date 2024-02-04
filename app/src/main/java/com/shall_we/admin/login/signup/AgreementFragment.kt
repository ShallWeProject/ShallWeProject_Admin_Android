package com.shall_we.admin.login.signup

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
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
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.regions.Regions
import com.shall_we.admin.App
import com.shall_we.admin.App.Companion.context
import com.shall_we.admin.BuildConfig
import com.shall_we.admin.MainActivity
import com.shall_we.admin.R
import com.shall_we.admin.databinding.FragmentAgreementBinding
import com.shall_we.admin.login.LoginFragment
import com.shall_we.admin.login.data.AuthRes
import com.shall_we.admin.login.data.IdentificationUploadReq
import com.shall_we.admin.login.data.IdentificationUploadUri
import com.shall_we.admin.login.data.MessageRes
import com.shall_we.admin.login.data.SignUpReq
import com.shall_we.admin.login.retrofit.IAuthSignIn
import com.shall_we.admin.login.retrofit.IAuthSignUp
import com.shall_we.admin.login.retrofit.IdentificationUploadService
import com.shall_we.admin.login.retrofit.SignInService
import com.shall_we.admin.login.retrofit.SignUpService
import com.shall_we.admin.login.retrofit.ValidCodeService
import com.shall_we.admin.retrofit.RESPONSE_STATE
import com.shall_we.admin.utils.S3Util
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

class AgreementFragment : Fragment() {
    private lateinit var binding : FragmentAgreementBinding

    private lateinit var name : String
    private lateinit var phone : String
    private lateinit var password : String

    private lateinit var identificationUploadUri: IdentificationUploadUri


    private var selectedImageUri: Uri = Uri.EMPTY
    private var filename: String = ""
    private var ext: String = ""
    private lateinit var file: File

    private lateinit var identificationReq : IdentificationUploadReq

    private

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
                    responseState, responseCode, responseBody ->
                when(responseState){
                    RESPONSE_STATE.OKAY -> {
                        Log.d("retrofit", "category api : ${responseCode.hashCode()}")
                        if(responseCode.hashCode() == 200){
                            // 이미지 s3에 업로드
                            if (responseBody != null) {
                                upload(identificationUploadUri.identificationUri)
                                var identificationFileName = "uploads/$filename.$ext"
                                upload(identificationUploadUri.businessRegistrationUri)
                                var businessRegistrationFileName = "uploads/$filename.$ext"
                                upload(identificationUploadUri.bankbookUri)
                                var bankbookFileName = "uploads/$filename.$ext"
                                identificationReq = IdentificationUploadReq(identificationFileName, businessRegistrationFileName, bankbookFileName)
                                setUpAccessToken(responseBody.data.accessToken)
                                postIdenficicationUpload(identificationReq)
                            }
                        }
                        else if (responseCode == 400){
                            Toast.makeText(context,"${responseCode}. 다시 시도해 주세요",Toast.LENGTH_LONG).show()
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

        identificationUploadUri = arguments?.getParcelable("identificationUploadUri")!!

        Log.d("identificationUploadUri", "$identificationUploadUri")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAgreement()
    }

    private fun upload(selectedImageUri : Uri) {
        if (selectedImageUri != null) {
            this.selectedImageUri = selectedImageUri
            parseUri(selectedImageUri)
            file = File(selectedImageUri.toString())
        }

        S3Util.instance
            .setKeys(BuildConfig.access_key, BuildConfig.secret_key)
            .setRegion(Regions.AP_NORTHEAST_2)
            .uploadWithTransferUtility(
                this.context,
                bucketName = "shallwebucket",
                folder = "uploads",
                file = file,
                fileName = "$filename.$ext",
                object : TransferListener {
                    override fun onStateChanged(id: Int, state: TransferState?) {
                    }
                    override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
                    }
                    override fun onError(id: Int, ex: java.lang.Exception?) {
                    }
                }
            );
        Log.d("S3Util", "hi")
    }

    private fun parseUri(selectedImageUri: Uri) {
        val fileForName = File(selectedImageUri.toString())
        val currentTime: Long = System.currentTimeMillis() // ms로 반환
        println(currentTime)
        val dateFormat = SimpleDateFormat("HHmmss")

        val formattedTime: String = dateFormat.format(Date(currentTime))

        // Print the formatted time
        println(formattedTime)
        filename = fileForName.nameWithoutExtension + formattedTime
        ext = fileForName.extension
    }

    private fun postIdenficicationUpload(uploadImage: IdentificationUploadReq) {
        Log.d("postmemoryphoto",uploadImage.toString())
        IdentificationUploadService().postIdenficicationUpload(
            uploadImage = uploadImage,
            completion = {
                    responseState, responseBody ->
                when(responseState){
                    RESPONSE_STATE.OKAY -> {
                        Log.d("retrofit", "category api : ${responseBody}")
                        deleteAccessToken()
                    }

                    RESPONSE_STATE.FAIL -> {
                        Log.d("retrofit", "api 호출 에러")
                    }
                }
            })
    }

    private fun setUpAccessToken(accessToken: String){
        val sharedPref = context?.getSharedPreferences("com.shall_we.admin", Context.MODE_PRIVATE)
        sharedPref?.edit()?.putString("access_token", accessToken)?.apply()

        App.accessToken = sharedPref?.getString("access_token", null)
    }

    private fun deleteAccessToken(){
        val sharedPref = context?.getSharedPreferences("com.shall_we.admin", Context.MODE_PRIVATE)
        sharedPref?.edit()?.putString("access_token", null)?.apply()

        App.accessToken = sharedPref?.getString("access_token", null)

        requireActivity().supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE) // 현재 스택에 있는 모든 프래그먼트를 제거합니다.

        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainerView3, SignupSuccessFragment())
        transaction.addToBackStack(null) // 이전 상태를 백 스택에 추가합니다.
        transaction.commit()
    }
}