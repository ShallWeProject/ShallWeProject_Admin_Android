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
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.shall_we.admin.R
import com.shall_we.admin.databinding.FragmentManagingProductBinding
import com.shall_we.admin.login.retrofit.IdentificationUploadService
import com.shall_we.admin.product.data.AdminExperienceReq
import com.shall_we.admin.product.data.ExplanationReq
import com.shall_we.admin.product.data.Product
import com.shall_we.admin.product.retrofit.AdminExperienceService
import com.shall_we.admin.product.retrofit.ProductImgUploadService
import com.shall_we.admin.retrofit.BodyData
import com.shall_we.admin.retrofit.RESPONSE_STATE
import com.shall_we.admin.schedule.CustomAlertDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


class ManagingProductFragment : Fragment() {
    private var _binding: FragmentManagingProductBinding? = null
    private val binding get() = _binding!!

//    private lateinit var adapter: GiftImgAdapter

    private var expCategory: String? = null

    private var selectedImageUri: Uri = Uri.EMPTY
    private var curr1Uri: Uri = Uri.EMPTY
    private var curr2Uri: Uri = Uri.EMPTY
    private var curr3Uri: Uri = Uri.EMPTY
    private var curr1ImgKey: String? = null
    private var curr2ImgKey: String? = null
    private var curr3ImgKey: String? = null
    private var filename: String = ""
    private var ext: String = ""
    private lateinit var file: File
    private var path: Uri = Uri.EMPTY

    private var giftImgUri: Uri = Uri.EMPTY
    private var imgUriList: MutableList<Uri> = mutableListOf()
    private var giftKeyList: MutableList<String> = mutableListOf()

    var idx: Int = -1
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
        requestPermission()

//        binding.rvGiftImg.adapter = GiftImgAdapter(requireContext())
//        binding.rvGiftImg.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)


        val list = resources.getStringArray(R.array.select_category).toList()
        _binding!!.spinner.adapter =
            CategorySpinnnerAdapter(requireContext(), R.layout.dropdown_item, list)
        _binding!!.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                expCategory = binding.spinner.getItemAtPosition(p2).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                expCategory = null
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

        val noteEditText: EditText? = view?.findViewById(R.id.caution)

        // 텍스트 변경을 감지하는 TextWatcher 설정
        binding.caution.addTextChangedListener(object : TextWatcher {
            var lastLength = 0

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                lastLength = s?.length ?: 0
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val cursorPosition = binding.caution.selectionStart

                // 사용자가 엔터를 입력한 경우 새로운 줄에 "• "를 추가
                if (s.toString().endsWith("\n")) {
                    s?.insert(cursorPosition, "• ")
                }

                // 사용자가 백스페이스를 입력한 경우
                if (s?.length!! < lastLength) {
                    // 현재 커서 위치의 줄을 가져옴
                    val currentLine = s?.subSequence(s.lastIndexOf("\n", cursorPosition - 1) + 1, cursorPosition) ?: ""
                    Log.d("currentLine", "$currentLine")
                    // 해당 줄이 비어 있고 "• "로 시작하면 해당 줄을 삭제
                    if (currentLine.isBlank() && s.toString().startsWith("•", cursorPosition)) {
                        val start = s.lastIndexOf("\n", cursorPosition - 1) + 1
                        val end = cursorPosition + 2 // "• "까지 포함
                        s.delete(start, end)
                    }
                }

                // 맨 윗줄의 "• "는 무조건 남도록 함
                if (s.isNullOrBlank() || !s.startsWith("• ")) {
                    if (!s.isNullOrBlank() && !s.toString().startsWith("• ")) {
                        s.insert(0, "• ") // "• "를 삽입
                    }
                }
            }
        })


        binding.giftImgKey.setOnClickListener {
            openGallery(0)
        }

        binding.tvCurr1Img.setOnClickListener {
            openGallery(1)
        }

        binding.tvCurr2Img.setOnClickListener {
            openGallery(2)
        }

        binding.tvCurr3Img.setOnClickListener {
            openGallery(3)
        }

        binding.btnDel1.setOnClickListener {
            binding.curr1Img.setImageResource(R.drawable.splash_icon)
            curr1ImgKey = ""
        }

        binding.btnDel2.setOnClickListener {
            binding.curr2Img.setImageResource(R.drawable.splash_icon)
            curr2ImgKey = ""
        }

        binding.btnDel3.setOnClickListener {
            binding.curr3Img.setImageResource(R.drawable.splash_icon)
            curr3ImgKey = ""
        }

        binding.btnSave.setOnClickListener {

            // 이미지 업로드 변경사항 생긴 커리큘럼만 imgUpload 해야함
            imgUpload(curr1Uri, 1)
            imgUpload(curr2Uri, 2)
            imgUpload(curr3Uri, 3)
            imgListUpload(imgUriList)

            //popupMsg("변경사항이 저장되었습니다.")

            val alertDialog = CustomAlertDialog(requireContext(), R.layout.custom_popup_layout)
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
                val subtitle = binding.subtitle.text.toString()
                val title = binding.product.text.toString()
                val priceText = binding.price.text.toString()
                val price: Int? = priceText.toIntOrNull()
                val giftImgKey = giftKeyList
                val description = binding.description.text.toString()
                val curriculum1 = binding.tvCurr1.text.toString()
                val curriculum1desc = binding.curr1Description.text.toString()
                val curriculum1Img = curr1ImgKey
                val curriculum2 = binding.tvCurr2.text.toString()
                val curriculum2desc = binding.curr2Description.text.toString()
                val curriculum2Img = curr2ImgKey
                val curriculum3 = binding.tvCurr3.text.toString()
                val curriculum3desc = binding.curr3Description.text.toString()
                val curriculum3Img = curr3ImgKey
                val location = binding.address.text.toString()
                val note = binding.caution.text.toString()

                val explanationList = listOf(
                    ExplanationReq(
                        stage = curriculum1,
                        description = curriculum1desc,
                        explanationKey = curriculum1Img
                    ),
                    ExplanationReq(
                        stage = curriculum2,
                        description = curriculum2desc,
                        explanationKey = curriculum2Img
                    ),
                    ExplanationReq(
                        stage = curriculum3,
                        description = curriculum3desc,
                        explanationKey = curriculum3Img
                    )
                )

                val productListData = AdminExperienceReq(
                    subtitle = subtitle,
                    expCategory = expCategory!!,
                    title = title,
                    giftImgKey = giftImgKey,
                    description = description,
                    explanation = explanationList,
                    location = location,
                    price = price,
                    note = note
                )

                Log.d("ProductListData", "$productListData")

                RetrofitPostCall(productListData)
                dialog.dismiss()
            }
            dialog.show()


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

        binding.rvGiftImg.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            val giftImgAdapter = GiftImgAdapter(imgUriList)
            giftImgAdapter.setOnItemClickListener(object : GiftImgAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    Log.d("remove", "remove")
                    imgUriList.removeAt(position)
                    //adapter?.notifyDataSetChanged()
                    giftImgAdapter.notifyDataSetChanged()
                    //binding.rvGiftImg.adapter = GiftImgAdapter(imgUriList)
                }
            })
            adapter = giftImgAdapter
        }
    }

    private fun imgUpload(uri: Uri, step: Int) {
        file = File(uri.toString())

        Log.d("imgUpload","$uri, $file")
        val filename = file.nameWithoutExtension
        val ext = file.extension

        val data = BodyData(ext = ext, dir = "explanation", filename = filename)
        Log.d("data", "$data")

        ProductImgUploadService().getImgUrl(data) { responseState, responseBody ->
            when (responseState) {
                RESPONSE_STATE.OKAY -> {
                    Log.d("getImgUrl", "$responseBody")
                    val imageKey = responseBody?.asJsonObject?.get("imageKey").toString().replace("\"", "")
                    val endpoint =responseBody?.asJsonObject?.get("presignedUrl").toString().substringAfter("https://shallwebucket.s3.ap-northeast-2.amazonaws.com/")
                        .removeSuffix("\"")

                    val mediaType = "image/${ext}".toMediaTypeOrNull()
                    var requestBody :RequestBody? = null

                    when (step) {
                        1 -> {
                            requestBody = File(curr1Uri.toString()).asRequestBody(mediaType)
                            curr1ImgKey = imageKey
                            Log.d("currImgKey","$imageKey")
                        }
                        2 -> {
                            requestBody = File(curr2Uri.toString()).asRequestBody(mediaType)
                            curr2ImgKey = imageKey
                            Log.d("currImgKey","$imageKey")
                        }
                        3 -> {
                            requestBody = File(curr3Uri.toString()).asRequestBody(mediaType)
                            curr3ImgKey = imageKey
                            Log.d("currImgKey","$imageKey")
                        }
                    }



                    Log.d("uploadImg requestBody", "$requestBody")

                    ProductImgUploadService().uploadImg(
                        data = requestBody!!,
                        url = "https://shallwebucket.s3.ap-northeast-2.amazonaws.com/",
                        endpoint = endpoint
                    ) { uploadState, _ ->
                        // Handle the upload completion if needed
                        when (uploadState) {
                            RESPONSE_STATE.OKAY -> {
                                Log.d("uploadImg currkey", "$curr1ImgKey")
                                Log.d("retrofit", "uploadImg api : ${uploadState}")
                            }

                            RESPONSE_STATE.FAIL -> {
                                Log.d("uploadImg currkey", "$curr1ImgKey")
                                Log.d("retrofit", "uploadImg api 호출 에러")
                            }
                        }
                    }
                }

                RESPONSE_STATE.FAIL -> {
                    Log.d("retrofit", "getImgUrl 호출 에러")
                }
            }
        }
    }

    private fun imgListUpload(list: MutableList<Uri>) {
        Log.d("imgListUpload", "$list")
        for (i in list.indices) {
//            if (list[i] in preImgList)
//                continue

            val file = File(list[i].toString())

            Log.d("imgUpload", "$file")
            val filename = file.nameWithoutExtension
            val ext = file.extension

            val data = BodyData(ext = ext, dir = "explanation", filename = filename)
            Log.d("data", "$data")

            ProductImgUploadService().getImgUrl(data) { responseState, responseBody ->
                when (responseState) {
                    RESPONSE_STATE.OKAY -> {
                        Log.d("getImgUrl", "$responseBody")
                        val imageKey = responseBody?.asJsonObject?.get("imageKey").toString().replace("\"", "")
                        val endpoint = responseBody?.asJsonObject?.get("presignedUrl").toString().substringAfter("https://shallwebucket.s3.ap-northeast-2.amazonaws.com/")
                            .removeSuffix("\"")

                        val mediaType = "image/${ext}".toMediaTypeOrNull()
                        val requestBody = file.asRequestBody(mediaType)
                        giftKeyList.add(imageKey)
                        Log.d("giftImgKey", "$imageKey")
                        Log.d("giftImgKeyList", "$giftKeyList")

                        Log.d("uploadImg requestBody", "$requestBody")

                        ProductImgUploadService().uploadImg(
                            data = requestBody!!,
                            url = "https://shallwebucket.s3.ap-northeast-2.amazonaws.com/",
                            endpoint = endpoint
                        ) { uploadState, _ ->
                            // Handle the upload completion if needed
                            when (uploadState) {
                                RESPONSE_STATE.OKAY -> {
                                    Log.d("uploadImg currkey", "$imageKey")
                                    Log.d("retrofit", "uploadImg api : ${uploadState}")
                                }

                                RESPONSE_STATE.FAIL -> {
                                    Log.d("uploadImg currkey", "$imageKey")
                                    Log.d("retrofit", "uploadImg api 호출 에러")
                                }
                            }
                        }
                    }

                    RESPONSE_STATE.FAIL -> {
                        Log.d("retrofit", "getImgUrl 호출 에러")
                    }
                }
            }
        }
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
    private fun openGallery(requestCode: Int) {
        Log.d("gallery", "갤러리")
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, requestCode)
    }

    // startActivityForResult로부터 결과 처리
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data!!
            // path = selectedImageUri

            //imgUriList.add(path) // 나중에 선택된 리스트가 오른쪽에 추가
            //newImgList = mutableListOf(path.toString() + newImgList) // 나중에 선택된 리스트가 왼쪽에 추가
            giftImgUri =
                selectedImageUri?.let {
                    getImageAbsolutePath(
                        it,
                        requireContext()
                    )?.toUri()
                }!!// 선택한 이미지의 경로를 구하는 함수 호출
            Log.d("giftImgUri", "$giftImgUri")
            Log.d("path", "$path")
            imgUriList.add(giftImgUri)
            binding.rvGiftImg.adapter?.notifyDataSetChanged()
            //binding.rvGiftImg.adapter = GiftImgAdapter(imgUriList)
        }
        // 1단계 사진 첨부
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            // 선택한 이미지의 Uri 가져오기
            selectedImageUri = data?.data!!
            path = selectedImageUri

            Glide.with(requireContext())
                .load(path) // 이미지의 Uri를 로드합니다.
                .into(binding.curr1Img);

            curr1Uri =
                selectedImageUri?.let {
                    getImageAbsolutePath(
                        it,
                        requireContext()
                    )?.toUri()
                }!!// 선택한 이미지의 경로를 구하는 함수 호출
        }

        else if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data!!
            path = selectedImageUri

            Glide.with(requireContext())
                .load(path) // 이미지의 Uri를 로드합니다.
                .into(binding.curr2Img);

            curr2Uri =
                selectedImageUri?.let {
                    getImageAbsolutePath(
                        it,
                        requireContext()
                    )?.toUri()
                }!!
        }

        else if (requestCode == 3 && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data!!
            path = selectedImageUri

            Glide.with(requireContext())
                .load(path) // 이미지의 Uri를 로드합니다.
                .into(binding.curr3Img);

            curr3Uri =
                selectedImageUri?.let {
                    getImageAbsolutePath(
                        it,
                        requireContext()
                    )?.toUri()
                }!!
            Log.d("curr3Uri", "$curr3Uri")
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

    }

    private fun checkAllFilled(): Boolean {
        if (true) {
            return true
        }
        else {
            // toast message 띄우기
            return false
        }
    }

    private fun navigateToOriginalFragment() {
        parentFragmentManager.popBackStack("fragment", 0) // 백 스택에서 이전 Fragment로 이동
    }


    private fun RetrofitPostCall(adminExperienceReq: AdminExperienceReq){
        AdminExperienceService().postAdminExperienceGift(adminExperienceReq = adminExperienceReq, completion =  { responseState ->
            when (responseState) {
                RESPONSE_STATE.OKAY -> {
                    //popupMsg("상품이 등록되었습니다.")
                    Log.d("retrofit", "postAdminExperienceGift success")
                }

                RESPONSE_STATE.FAIL -> {
                    //popupMsg("$responseState")
                    Log.d("retrofit", "api 호출 에러")
                }
            }
        })
    }
}