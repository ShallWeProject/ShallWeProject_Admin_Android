package com.shall_we.admin.product

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
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
import androidx.fragment.app.Fragment
import com.shall_we.admin.R
import com.shall_we.admin.databinding.FragmentManagingProductBinding
import com.shall_we.admin.product.data.AdminExperienceReq
import com.shall_we.admin.product.data.ExplanationRes
import com.shall_we.admin.product.retrofit.AdminExperienceService
import com.shall_we.admin.retrofit.RESPONSE_STATE
import com.shall_we.admin.schedule.CustomAlertDialog


class ManagingProductFragment : Fragment() {
    private var _binding: FragmentManagingProductBinding? = null
    private val binding get() = _binding!!
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


        binding.btnSave.setOnClickListener {
            val subtitle = binding.subtitle.text.toString()
            val expCategory = binding.expCategory.text.toString()
            val title = binding.product.text.toString()
            val priceText = binding.price.text.toString()
            val price: Int? = priceText.toIntOrNull()
            //val giftImgKey = binding.thumbnail.text.toString()
            val description = binding.description.text.toString()
//            val contextImg = binding.contextImg.text.toString()
            val curriculum1 = binding.curr1Description.text.toString()
            val curriculum1Img = binding.curr1Img.text.toString()
            val curriculum2 = binding.curr2Description.text.toString()
            val curriculum2Img = binding.curr2Img.text.toString()
            val curriculum3 = binding.curr3Description.text.toString()
            val curriculum3Img = binding.curr3Img.text.toString()
            val curriculum4 = binding.tvCurr4.text.toString()
            val curriculum4Img = binding.curr4Img.text.toString()
            val location = binding.address.text.toString()
            val caution = binding.caution.text.toString()

            val explanationList = listOf(
                ExplanationRes(stage = "1", description = curriculum1, explanationUrl = curriculum1Img),
                ExplanationRes(stage = "2", description = curriculum2, explanationUrl = curriculum2Img),
                ExplanationRes(stage = "3", description = curriculum3, explanationUrl = curriculum3Img),
                ExplanationRes(stage = "4", description = curriculum4, explanationUrl = curriculum4Img)
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
            popupMsg("변경사항이 저장되었습니다.")
            RetrofitPostCall(productListData)

            // 수정하기 api


            Log.d("ProductListData", "$productListData")

        }

        binding.btnBack.setOnClickListener {
            val productFragment = ProductFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, productFragment)
                .addToBackStack(null)
                .commit()
            //navigateToOriginalFragment()
            //Toast.makeText(requireContext(), "눌렀음..", Toast.LENGTH_SHORT).show()
        }
        return binding.root
    }

    fun popupMsg(msg : String){
        val newAlertDialog = CustomAlertDialog(requireContext(), R.layout.custom_popup_layout)
        newAlertDialog.setDialogContent(msg)
        val newDialog = newAlertDialog.create()

        val layoutParams = WindowManager.LayoutParams()
//        layoutParams.copyFrom(newAlertDialog.window?.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        layoutParams.gravity = Gravity.BOTTOM // 중앙으로 정렬
        newDialog.window?.attributes = layoutParams
        newDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 이 부분을 수정

        newAlertDialog.setNegativeButton("확인") { v ->
            newDialog.dismiss()
        }
        newDialog.show()
    }

    private fun navigateToOriginalFragment() {
        parentFragmentManager.popBackStack("fragment", 0) // 백 스택에서 이전 Fragment로 이동
    }

    fun RetrofitPostCall(adminExperienceReq: AdminExperienceReq){
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

