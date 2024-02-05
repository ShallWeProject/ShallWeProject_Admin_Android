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
import com.shall_we.admin.App
import com.shall_we.admin.R
import com.shall_we.admin.databinding.FragmentAgreementBinding
import com.shall_we.admin.login.data.IdentificationUploadReq
import com.shall_we.admin.login.data.IdentificationUploadUri
import com.shall_we.admin.login.data.SignUpReq
import com.shall_we.admin.login.retrofit.IdentificationUploadService
import com.shall_we.admin.login.retrofit.SignInService
import com.shall_we.admin.login.retrofit.SignUpService
import com.shall_we.admin.login.retrofit.ValidCodeService
import com.shall_we.admin.retrofit.BodyData
import com.shall_we.admin.retrofit.RESPONSE_STATE
import com.shall_we.admin.utils.S3Util
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

class AgreementFragment : Fragment() {
    private lateinit var binding : FragmentAgreementBinding

    private lateinit var name : String
    private lateinit var phone : String
    private lateinit var password : String

    private lateinit var identificationUploadUri: IdentificationUploadUri

    private var ext: String = "jpg"

    private lateinit var identificationFileName: String
    private lateinit var businessRegistrationFileName: String
    private lateinit var bankbookFileName: String


    private var imgUrlCounter = 0

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
                                val identificationUri = identificationUploadUri.identificationUri
                                val businessRegistrationUri = identificationUploadUri.businessRegistrationUri
                                val bankbookUri = identificationUploadUri.bankbookUri

                                // Use coroutines to execute image upload tasks sequentially
                                CoroutineScope(Dispatchers.Main).launch {
                                    imgUpload(identificationUri)
                                    imgUpload(businessRegistrationUri)
                                    imgUpload(bankbookUri)

                                    // All image uploads completed, proceed with the next steps
                                    setUpAccessToken(responseBody.data.accessToken)
                                    identificationReq = IdentificationUploadReq(
                                        identificationFileName,
                                        businessRegistrationFileName,
                                        bankbookFileName
                                    )
                                    postIdenficicationUpload(identificationReq)
                                }
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

    private suspend fun imgUpload(identificationUploadUri: Uri) {
        imgUrlCounter++
        val fileName = identificationUploadUri.toString().removeSuffix(".jpg").substringAfterLast("/")
        val ext = "jpg"
        val file = File(identificationUploadUri.toString())

        val data = BodyData(ext = ext, dir = "uploads", filename = fileName)
        Log.d("data", "$data")

        val response = withContext(Dispatchers.IO) {
            IdentificationUploadService().getImgUrl(data)
        }

        when (response.first) {
            RESPONSE_STATE.OKAY -> {
                val imageKey = response.second.substringAfter("\"").removeSuffix("\"")
                val presignedUrl =
                    response.third.substringAfter("https://shallwebucket.s3.ap-northeast-2.amazonaws.com/")
                        .removeSuffix("\"")
                getImgUrlResult(imageKey, presignedUrl)

                val mediaType = "image/*".toMediaTypeOrNull()
                val requestBody = file.asRequestBody(mediaType)

                val uploadResponse = withContext(Dispatchers.IO) {
                    IdentificationUploadService().uploadImg(
                        data = requestBody,
                        url = "https://shallwebucket.s3.ap-northeast-2.amazonaws.com/",
                        endpoint = presignedUrl
                    ) { uploadState, _ ->
                        // Handle the upload completion if needed
                        when (uploadState) {
                            RESPONSE_STATE.OKAY -> {
                                Log.d("retrofit", "uploadImg api : ${uploadState}")
                            }

                            RESPONSE_STATE.FAIL -> {
                                Log.d("retrofit", "uploadImg api 호출 에러")
                            }
                        }
                    }
                }
            }
            RESPONSE_STATE.FAIL -> {
                Log.d("retrofit", "getImgUrl 호출 에러")
            }
        }
    }

    private fun getImgUrlResult(imageKey: String, presignedUrl: String) {
        if (imgUrlCounter == 1) {
            identificationFileName = imageKey
        }
        else if (imgUrlCounter == 2) {
            businessRegistrationFileName = imageKey
        }
        else if (imgUrlCounter == 3) {
            bankbookFileName = imageKey
        }
    }

    private fun postIdenficicationUpload (uploadImage: IdentificationUploadReq) {
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