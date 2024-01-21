package com.shall_we.admin.login.signup

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.regions.Regions
import com.shall_we.admin.R
import com.shall_we.admin.databinding.FragmentShopInfoBinding
import com.shall_we.admin.utils.S3Util

class ShopInfoFragment : Fragment() {

    private lateinit var name : String
    private lateinit var phoneNumber : String
    private lateinit var password : String

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

        requestPermission()

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

    // 사진 접근 권한 요청
    private fun requestPermission() {
        val locationResultLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
            if (!it) {
                Toast.makeText(view?.context, "스토리지에 접근 권한을 허가해주세요", Toast.LENGTH_SHORT).show()
            }
        }
        locationResultLauncher.launch(
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    // 갤러리 열기
//    private fun openGallery() {
//        Log.d("gallery", "갤러리")
//        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//        startActivityForResult(intent, PICK_IMAGE_REQUEST)
//    }

//    private fun upload() {
//        S3Util.instance
//            .setKeys(access_key, secret_key)
//            .setRegion(Regions.AP_NORTHEAST_2)
//            .uploadWithTransferUtility(
//                this.context,
//                bucketName = "shallwebucket",
//                folder = "uploads",
//                file = file,
//                fileName = "$filename.$ext",
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
//        val uploadPhotoArray = UploadPhotoArray("uploads/$filename.$ext", giftData[giftIdx].idx)
//        Log.d("upload photo array", "$uploadPhotoArray")
//        postMemoryPhoto(uploadPhotoArray)
//        //Toast.makeText(view?.context , "사진이 추가되었습니다.", Toast.LENGTH_SHORT).show()
//        Log.d("postMemoryPhoto", "idx: ${giftData[giftIdx].idx},  ${giftData[giftIdx].date} 날짜에 업로드 완료 key: uploads/$filename.$ext")
//        //val modDate: String = giftData[giftIdx].date.replace(".", "-")
//        //getMemoryPhoto(modDate, giftData[giftIdx].time)
//    }

}