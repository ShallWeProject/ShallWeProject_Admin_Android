package com.shall_we.admin.login.signup

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.regions.Regions
import com.shall_we.admin.BuildConfig
import com.shall_we.admin.R
import com.shall_we.admin.databinding.FragmentShopInfoBinding
import com.shall_we.admin.login.data.UploadPhotoArray
import com.shall_we.admin.utils.S3Util
import java.io.File


class ShopInfoFragment : Fragment() {

    private lateinit var name : String
    private lateinit var phoneNumber : String
    private lateinit var password : String

    private val PICK_IMAGE_REQUEST = 1 // 요청 코드

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
        // Inflate the layout for this fragment
        val binding = FragmentShopInfoBinding.inflate(inflater,container,false)

        phoneNumber = arguments?.getString("phone", "").toString()
        name = arguments?.getString("name","").toString()
        password = arguments?.getString("password","").toString()

        binding.idCardBtn.setOnClickListener {
            openGallery()
        }
        binding.ownerBtn.setOnClickListener {
            openGallery()
        }
        binding.bankbookBtn.setOnClickListener {
            openGallery()
        }
        binding.btnNextShopInfo.setOnClickListener {
            val newFragment = AgreementFragment() // 전환할 다른 프래그먼트 객체 생성
            val bundle = Bundle()
            bundle.putString("name",name)
            bundle.putString("phone",phoneNumber)
            bundle.putString("password",password)
            newFragment.arguments = bundle

            // 프래그먼트 전환
            parentFragmentManager.beginTransaction()
                .add(R.id.fragmentContainerView3, newFragment)
                .addToBackStack(null)
                .commit()
        }
        return binding.root
    }

    private fun openGallery() {
        Log.d("gallery", "갤러리")
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

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
//                upload()
            }
        }
    }

//    private fun upload() {
//        S3Util.instance
//            .setKeys(BuildConfig., secret_key)
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
////        val uploadPhotoArray = UploadPhotoArray("uploads/$filename.$ext", giftData[giftIdx].idx)
////        Log.d("upload photo array", "$uploadPhotoArray")
////        postPhoto(uploadPhotoArray)
////        Toast.makeText(view?.context , "사진이 추가되었습니다.", Toast.LENGTH_SHORT).show()
//    }

//    private fun postPhoto(uploadPhotoArray: UploadPhotoArray) {
//        RetrofitManager.instance.postMemoryPhoto(
//            uploadPhotoArray = uploadPhotoArray,
//            completion = { responseState ->
//                when (responseState) {
//                    RESPONSE_STATE.OKAY -> {
//                        Log.d("retrofit", "postmemoryphoto api : ${responseState}")
//                    }
//
//                    RESPONSE_STATE.FAIL -> {
//                        Log.d("retrofit", "api 호출 에러")
//                    }
//                }
//            })
//    }

    private fun parseUri(selectedImageUri: Uri) {
        val fileForName = File(selectedImageUri.toString())
        filename = fileForName.nameWithoutExtension
        ext = fileForName.extension
    }

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

}