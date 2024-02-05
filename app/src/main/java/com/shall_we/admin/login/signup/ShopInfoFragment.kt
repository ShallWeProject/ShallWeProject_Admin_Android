package com.shall_we.admin.login.signup

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.shall_we.admin.R
import com.shall_we.admin.databinding.FragmentShopInfoBinding
import com.shall_we.admin.login.data.IdentificationUploadUri

class ShopInfoFragment : Fragment() {

    private lateinit var binding: FragmentShopInfoBinding

    private lateinit var name : String
    private lateinit var phoneNumber : String
    private lateinit var password : String

    private var selectedImageUri: Uri = Uri.EMPTY
    private var path: Uri = Uri.EMPTY

    private var identificationUri : Uri = Uri.EMPTY
    private var businessRegistrationUri : Uri = Uri.EMPTY
    private var bankbookUri : Uri = Uri.EMPTY

    private var firFlag = false
    private var secFlag = false
    private var thiFlag = false

    private lateinit var identificationUploadUri: IdentificationUploadUri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentShopInfoBinding.inflate(inflater,container,false)

        requestPermission()

        phoneNumber = arguments?.getString("phone", "").toString()
        name = arguments?.getString("name","").toString()
        password = arguments?.getString("password","").toString()

        binding.idCardBtn.setOnClickListener {
            openGallery(0)
        }
        binding.ownerBtn.setOnClickListener {
            openGallery(1)
        }
        binding.bankbookBtn.setOnClickListener {
            openGallery(2)
        }

        // 다음 버튼 클릭 시
        binding.btnNextShopInfo.setOnClickListener {
            // 이미지 다 올렸으면
            if(firFlag && secFlag && thiFlag){
                if(identificationUri != Uri.EMPTY && businessRegistrationUri != Uri.EMPTY && bankbookUri != Uri.EMPTY){
                    identificationUploadUri = IdentificationUploadUri(identificationUri, businessRegistrationUri, bankbookUri)
                }
                val newFragment = AgreementFragment() // 전환할 다른 프래그먼트 객체 생성
                val bundle = Bundle()
                bundle.putString("name",name)
                bundle.putString("phone",phoneNumber)
                bundle.putString("password",password)
                bundle.putParcelable("identificationUploadUri", identificationUploadUri as Parcelable)
                newFragment.arguments = bundle

                // 프래그먼트 전환
                parentFragmentManager.beginTransaction()
                    .add(R.id.fragmentContainerView3, newFragment)
                    .addToBackStack(null)
                    .commit()
            }
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

    private fun openGallery(requestCode: Int) {
        Log.d("gallery", "갤러리")
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            // 선택한 이미지의 Uri 가져오기
            selectedImageUri = data?.data!!
            path = selectedImageUri
            identificationUri =
                    selectedImageUri?.let {
                        getImageAbsolutePath(
                                it,
                                requireContext()
                        )?.toUri()
                    }!! // 선택한 이미지의 경로를 구하는 함수 호출
            firFlag = true
            binding.idCardBtn.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this.requireContext(), R.color.rose600))
            changeBtnColor()
        }

        else if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            // 선택한 이미지의 Uri 가져오기
            selectedImageUri = data?.data!!
            path = selectedImageUri
            businessRegistrationUri =
                selectedImageUri?.let {
                    getImageAbsolutePath(
                        it,
                        requireContext()
                    )?.toUri()
                }!! // 선택한 이미지의 경로를 구하는 함수 호출
            secFlag = true
            binding.ownerBtn.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this.requireContext(), R.color.rose600))
            changeBtnColor()
            Log.d("shopinfo", "businessRegistrationUri = $businessRegistrationUri")
            Log.d("shopinfo", "path = $path")
        }

        else if(requestCode == 2 && resultCode == Activity.RESULT_OK){
            // 선택한 이미지의 Uri 가져오기
            selectedImageUri = data?.data!!
            path = selectedImageUri
            bankbookUri =
                selectedImageUri?.let {
                    getImageAbsolutePath(
                        it,
                        requireContext()
                    )?.toUri()
                }!! // 선택한 이미지의 경로를 구하는 함수 호출
            thiFlag = true
            binding.bankbookBtn.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this.requireContext(), R.color.rose600))
            changeBtnColor()
        }
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

    private fun changeBtnColor(){
        // 다음 버튼 색 & 활성화
        if(firFlag && secFlag && thiFlag){
            Log.d("shopinfo", "다음 버튼 색 & 활성화")
            binding.btnNextShopInfo.setBackgroundResource(R.drawable.btn_next)
            binding.btnNextShopInfo.isClickable = true
        }
        else{
            binding.btnNextShopInfo.setBackgroundResource(R.drawable.btn_next_black)
            binding.btnNextShopInfo.isClickable = false
        }
    }

}