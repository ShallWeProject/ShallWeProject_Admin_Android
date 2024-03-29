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
import com.shall_we.admin.product.data.AdminExperienceReq
import com.shall_we.admin.product.data.ExplanationReq
import com.shall_we.admin.product.data.Product
import com.shall_we.admin.product.retrofit.AdminExperienceService
import com.shall_we.admin.product.retrofit.ProductImgUploadService
import com.shall_we.admin.retrofit.BodyData
import com.shall_we.admin.retrofit.RESPONSE_STATE
import com.shall_we.admin.schedule.CustomAlertDialog
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


class EditingProductFragment : Fragment() {
    private var _binding: FragmentManagingProductBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: GiftImgAdapter

    private var expCategory: String? = null

    private var selectedImageUri: Uri = Uri.EMPTY
    private var curr1Uri: Uri = Uri.EMPTY
    private var curr2Uri: Uri = Uri.EMPTY
    private var curr3Uri: Uri = Uri.EMPTY
    private var curr1ImgKey: String? = null
    private var curr2ImgKey: String? = null
    private var curr3ImgKey: String? = null
    private var img1changed = false
    private var img2changed = false
    private var img3changed = false

    private var giftImgUri: Uri = Uri.EMPTY
    private var giftImgKey: String? = null
    private var giftImgChanged = false
    private var imgUriList: MutableList<Uri> = mutableListOf()

    private var filename: String = ""
    private var ext: String = ""
    private var path: Uri = Uri.EMPTY

    private var preImgList: MutableList<Uri> = mutableListOf()
    private var newImgList: MutableList<String>? = null
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
        requestPermission()

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
            if (img1changed) {
                imgUpload(curr1Uri, 1)
            }
            if (img2changed) {
                imgUpload(curr2Uri, 2)
            }
            if (img3changed) {
                imgUpload(curr3Uri, 3)
            }

            if (giftImgChanged) {
                imgListUpload(imgUriList)
            }


            Log.d("imgUpload 1", "$curr1ImgKey")


            //popupMsg("변경사항이 저장되었습니다.")

            val alertDialog = CustomAlertDialog(requireContext(), R.layout.custom_popup_layout)
            val dialog = alertDialog.create()
            alertDialog.setDialogContent("변경사항이 저장되었습니다.")

            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog.window?.attributes)
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
            layoutParams.gravity = Gravity.BOTTOM
            dialog.window?.attributes = layoutParams
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog.setNegativeButton("확인") { v ->

    //            Log.d("curriculum","$curr1ImgKey, $curr2ImgKey, $curr3ImgKey, $curr4ImgKey")
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


                // 수정하기 api
                RetrofitPutCall(idx, productListData)
                dialog.dismiss()
            }
            dialog.show()

            //Log.d("ProductListData", "$productListData")


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

//    private fun initGiftImg(resultData: List<String>) {
//        adapter = GiftImgAdapter(requireContext())
//        binding.rvGiftImg.adapter = adapter
//        adapter.setOnItemClickListener(this)
//
//        Log.d("retrofit", "initAlbum, $resultData")
//
//        adapter.datas = resultData
//        adapter.notifyDataSetChanged()
//
//        binding.rvGiftImg.apply {
//            setHasFixedSize(true)
//            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//            val giftImgAdapter = GiftImgAdapter(context)
//            giftImgAdapter.setOnItemClickListener(this@EditingProductFragment)
//            adapter = giftImgAdapter
//        }
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        idx = requireArguments().getInt("idx")
        RetrofitGetCall(idx)
    }

    private fun initView(data: Product) {
        binding.subtitle.setText(data.subtitle)
        val index = resources.getStringArray(R.array.select_category).toList()
            .indexOf(data.expCategory)
        if (index != -1) {
            binding.spinner.setSelection(index)
        }
        binding.product.setText(data.title)
        binding.price.setText(data.price.toString())
        binding.description.setText(data.description)

        for (i in data.giftImgKey?.indices!!) {
            imgUriList.add(data.giftImgKey[i].toUri())
            preImgList.add(data.giftImgKey[i].toUri())
        }
        newImgList = data.giftImgKey

        binding.rvGiftImg.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            val giftImgAdapter = GiftImgAdapter(imgUriList)
            giftImgAdapter.setOnItemClickListener(object : GiftImgAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
//                    newImgList = newImgList?.toMutableList()?.apply {
//                        removeAt(position)
//                    }
                    Log.d("remove", "remove")
                    imgUriList.removeAt(position)
                    //adapter?.notifyDataSetChanged()
                    giftImgAdapter.notifyDataSetChanged()
                    //binding.rvGiftImg.adapter = GiftImgAdapter(imgUriList)
                }
            })
            adapter = giftImgAdapter
        }


        if (data.explanation != null) {
            Log.d("data.explanation", "${data.explanation}")
            binding.tvCurr1.setText(data.explanation.get(0)?.stage ?: "")
            binding.curr1Description.setText(data.explanation?.get(0)?.description ?: "")
            Glide.with(this).load(data.explanation?.get(0)?.explanationUrl).into(binding.curr1Img)
            curr1ImgKey = data.explanation?.get(0)?.explanationUrl.toString()
                .substringAfter("https://shallwebucket.s3.ap-northeast-2.amazonaws.com/")
                .removeSuffix("\"")
            binding.tvCurr2.setText(data.explanation?.get(1)?.stage ?: "")
            binding.curr2Description.setText(data.explanation?.get(1)?.description ?: "")
            Glide.with(this).load(data.explanation?.get(1)?.explanationUrl).into(binding.curr2Img)
            curr2ImgKey = data.explanation?.get(1)?.explanationUrl.toString()
                .substringAfter("https://shallwebucket.s3.ap-northeast-2.amazonaws.com/")
                .removeSuffix("\"")
            binding.tvCurr3.setText(data.explanation?.get(2)?.stage ?: "")
            binding.curr3Description.setText(data.explanation?.get(2)?.description ?: "")
            Glide.with(this).load(data.explanation?.get(2)?.explanationUrl).into(binding.curr3Img)
            curr3ImgKey = data.explanation?.get(2)?.explanationUrl.toString()
                .substringAfter("https://shallwebucket.s3.ap-northeast-2.amazonaws.com/")
                .removeSuffix("\"")
        }
        binding.address.setText(data.location)
        binding.caution.setText(data.note)
        Log.d("currImgKey","$curr1ImgKey,$curr2ImgKey,$curr3ImgKey")
    }


    private fun imgUpload(uri: Uri, step: Int) {
        val file = File(uri.toString())

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
        alertDialog.setPositiveButton("확인") { v ->
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
            giftImgChanged = true
        }

        // 1단계 사진 첨부
        else if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
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
            img1changed = true
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
            img2changed = true
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
            img3changed = true
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
        // toast message 띄우기
        return binding.subtitle.text.isNotEmpty() && expCategory != null && binding.product.text.isNotEmpty() && binding.price.text.isNotEmpty() && binding.description.text.isNotEmpty() && binding.tvCurr1.text.isNotEmpty() && binding.curr1Description.text.isNotEmpty() && binding.tvCurr2.text.isNotEmpty() && binding.curr2Description.text.isNotEmpty() && binding.tvCurr3.text.isNotEmpty() && binding.curr3Description.text.isNotEmpty() && binding.address.text.isNotEmpty() && binding.caution.text.isNotEmpty()
    }

    private fun navigateToOriginalFragment() {
        parentFragmentManager.popBackStack("fragment", 0) // 백 스택에서 이전 Fragment로 이동
    }

    private fun RetrofitGetCall(idx: Int){
        AdminExperienceService().getAdminExperienceGift(experienceGiftId = idx) { responseState, responseBody ->
            when (responseState) {
                RESPONSE_STATE.OKAY -> {
                    Log.d("retrofit", "getAdminExperienceGift success")
                    if (responseBody != null) {
                        initView(responseBody)
                    }
                    Log.d("RetrofitGetCall body","$responseBody")
                }

                RESPONSE_STATE.FAIL -> {
                    Log.d("retrofit", "api 호출 에러")
                }
            }
        }
    }

    private fun RetrofitPutCall(idx: Int, adminExperienceReq: AdminExperienceReq){
        AdminExperienceService().putAdminExperienceGift(experienceGiftId = idx, adminExperienceReq = adminExperienceReq, completion =  { responseState ->
            when (responseState) {
                RESPONSE_STATE.OKAY -> {
                    Log.d("retrofit", "putAdminExperienceGift success")
                }

                RESPONSE_STATE.FAIL -> {
                    Log.d("retrofit", "api 호출 에러")
                }
            }
        })
    }
}

