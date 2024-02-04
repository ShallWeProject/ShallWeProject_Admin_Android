package com.shall_we.admin.product

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.regions.Regions
import com.shall_we.admin.R
import com.shall_we.admin.databinding.FragmentManagingProductBinding
import com.shall_we.admin.product.data.AdminExperienceReq
import com.shall_we.admin.product.data.ExplanationRes
import com.shall_we.admin.product.retrofit.AdminExperienceService
import com.shall_we.admin.retrofit.RESPONSE_STATE
import com.shall_we.admin.schedule.CustomAlertDialog
import com.shall_we.admin.utils.S3Util
import java.io.File


class ManagingProductFragment : Fragment() {
    private var _binding: FragmentManagingProductBinding? = null
    private val binding get() = _binding!!

    private var selectedImageUri: Uri = Uri.EMPTY
    private var filename: String = ""
    private var ext: String = ""
    private lateinit var file: File
    private var path: Uri = Uri.EMPTY

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

        val list = resources.getStringArray(R.array.select_category).toList()
        //val list = listOf("베이킹", "공예", "문화예술", "아웃도어", "스포츠")
        _binding!!.spinner.adapter =
            CategorySpinnnerAdapter(requireContext(), R.layout.dropdown_item, list)
        _binding!!.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val value = binding.spinner.getItemAtPosition(p2).toString()
                binding.expCategory.text = value
//                Toast.makeText(requireContext(), value, Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                binding.expCategory.text = null
            }
        }

        val priceEditText: EditText? = view?.findViewById(R.id.price)

        priceEditText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrBlank() || !s.endsWith("원")) {
                    // 텍스트가 비어있거나 "원"으로 끝나지 않으면 "원"을 자동으로 추가
                    s?.append("원")
                }
            }
        })

        binding.giftImgKey.setOnClickListener {

        }


        binding.btnSave.setOnClickListener {
            val subtitle = binding.subtitle.text.toString()
            val expCategory = binding.expCategory.text.toString()
            val title = binding.product.text.toString()
            val priceText = binding.price.text.toString()
            val price: Int? = priceText.toIntOrNull()
            //val giftImgKey = binding.thumbnail.text.toString()
            val description = binding.description.text.toString()
//            val contextImg = binding.contextImg.text.toString()
            val curriculum1 = binding.tvCurr1.text.toString()
            val curriculum1desc = binding.curr1Description.text.toString()
            val curriculum1Img = binding.curr1Img.text.toString()
            val curriculum2 = binding.tvCurr2.text.toString()
            val curriculum2desc = binding.curr2Description.text.toString()
            val curriculum2Img = binding.curr2Img.text.toString()
            val curriculum3 = binding.tvCurr3.text.toString()
            val curriculum3desc = binding.curr3Description.text.toString()
            val curriculum3Img = binding.curr3Img.text.toString()
            val curriculum4desc = binding.tvCurr4.text.toString()
            val curriculum4Img = binding.curr4Img.text.toString()
            val location = binding.address.text.toString()
            val caution = binding.caution.text.toString()

            val explanationList = listOf(
                ExplanationRes(stage = curriculum1, description = curriculum1desc, explanationUrl = curriculum1Img),
                ExplanationRes(stage = curriculum2, description = curriculum2desc, explanationUrl = curriculum2Img),
                ExplanationRes(stage = curriculum3, description = curriculum3desc, explanationUrl = curriculum3Img),
                ExplanationRes(stage = "", description = curriculum4desc, explanationUrl = curriculum4Img)
            )

            val productListData = AdminExperienceReq(
                subtitle = subtitle,
                expCategory = expCategory,
                //sttCategory = expCategory,
                title = title,
                giftImgKey = listOf("i"),
                description = description,
                explanation = explanationList,
                location = location,
                price = price,
                note = caution
            )

            Log.d("ProductListData", "$productListData")

            // 저장하기 api
            RetrofitPostCall(productListData)

            //popupMsg("상품이 등록되었습니다.")
            val alertDialog = CustomAlertDialog(requireContext(),R.layout.custom_popup_layout)
            val dialog = alertDialog.create()
            alertDialog.setDialogContent("상품이 등록되었습니다.")

            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog.window?.attributes)
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
            layoutParams.gravity = Gravity.BOTTOM
            dialog.window?.attributes = layoutParams
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog.setNegativeButton("확인") { v ->
                dialog.dismiss()
            }
            dialog.show()

            Log.d("ProductListData", "$productListData")

        }

        binding.btnBack.setOnClickListener {
            val productFragment = ProductFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, productFragment)
                .addToBackStack(null)
                .commit()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun popupMsg(msg: String) {
        val alertDialog = CustomAlertDialog(requireContext(),R.layout.custom_popup_layout)
        val dialog = alertDialog.create()
        alertDialog.setDialogContent(msg)

        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window?.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        layoutParams.gravity = Gravity.BOTTOM
        dialog.window?.attributes = layoutParams
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.setNegativeButton("확인") { v ->
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun requestPermission() {
        val locationResultLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
            if (!it) {
                Toast.makeText(view?.context, "스토리지에 접근 권한을 허가해주세요", Toast.LENGTH_SHORT).show()
            }
        }
        locationResultLauncher.launch(
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    private val PICK_IMAGE_REQUEST = 1 // 요청 코드

    // 갤러리 열기
    private fun openGallery() {
        Log.d("gallery", "갤러리")
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    // startActivityForResult로부터 결과 처리
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            // 선택한 이미지의 Uri 가져오기
            selectedImageUri = data?.data!!
            path = selectedImageUri
            selectedImageUri =
                selectedImageUri?.let {
                    getImageAbsolutePath(
                        it,
                        requireContext()
                    )?.toUri()
                }!!// 선택한 이미지의 경로를 구하는 함수 호출
            if (selectedImageUri != null) {
                this.selectedImageUri = selectedImageUri
                parseUri(selectedImageUri)
                file = File(selectedImageUri.toString())
                //upload()
            }
        }
    }
    // Uri에서 절대 경로 추출하기
    private fun getImageAbsolutePath(uri: Uri, context: Context): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        var cursor: Cursor? = null
        var path: String? = null
        try {
            cursor = context.contentResolver.query(uri, projection, null, null, null)
            cursor?.let {
                if (it.moveToFirst()) {
                    val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                    path = it.getString(columnIndex)
                    Log.d("getImageAbsolutePath", "절대경로 추출 uri = $path")
                }
            }
        } catch (e: Exception) {
            Log.e("getImageAbsolutePath", "절대경로 추출 오류: ${e.message}")
        } finally {
            cursor?.close()
        }
        return path
    }
    private fun parseUri(selectedImageUri: Uri) {
        val fileForName = File(selectedImageUri.toString())
        filename = fileForName.nameWithoutExtension
        ext = fileForName.extension
//        Log.d("fileForName", "$fileForName")
//        Log.d("filename, ext", "$filename $ext")
//        Log.d("Album Result", "$selectedImageUri")

    }

    @RequiresApi(Build.VERSION_CODES.O)
//
//    private fun upload() {
//        S3Util.instance
//            .setKeys(access_key, secret_key)
//            .setRegion(Regions.AP_NORTHEAST_2)
//            .uploadWithTransferUtility(
//                this.context,
//                "shallwebucket",
//                "uploads",
//                file,
//                object : TransferListener {
//                    override fun onStateChanged(id: Int, state: TransferState?) {
//                    }
//                    override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
//                    }
//                    override fun onError(id: Int, ex: java.lang.Exception?) {
//                    }
//                }
//            );
//        Log.d("S3Util", "hi")
//        val imageKey = "uploads/$filename.$ext"
//        Log.d("imageKey", "$imageKey")
//        //Toast.makeText(view?.context , "사진이 추가되었습니다.", Toast.LENGTH_SHORT).show()
//
//    }

    private fun navigateToOriginalFragment() {
        parentFragmentManager.popBackStack("fragment", 0) // 백 스택에서 이전 Fragment로 이동
    }

    private fun RetrofitPostCall(adminExperienceReq: AdminExperienceReq){
        AdminExperienceService().postAdminExperienceGift(adminExperienceReq = adminExperienceReq, completion =  { responseState ->
            when (responseState) {
                RESPONSE_STATE.OKAY -> {
                    Log.d("retrofit", "postAdminExperienceGift success")
                }

                RESPONSE_STATE.FAIL -> {
                    Log.d("retrofit", "api 호출 에러")
                }
            }
        })
    }
}