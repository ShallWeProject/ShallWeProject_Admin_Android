package com.shall_we.admin.product

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.RecyclerListener
import com.shall_we.admin.R
import com.shall_we.admin.databinding.FragmentProductBinding
import com.shall_we.admin.product.retrofit.AdminExperienceService
import com.shall_we.admin.retrofit.RESPONSE_STATE
import com.shall_we.admin.schedule.CustomAlertDialog


class ProductFragment : Fragment() {
    private var _binding: FragmentProductBinding? = null
    private val binding get() = _binding!!

    private var datas = mutableListOf<ProductData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductBinding.inflate(inflater, container, false)

        RetrofitGetCall()

        binding.btnAdd.setOnClickListener {
            navigateToNewFragment()
        }


        binding.btnDelete.setOnClickListener {

            // 여기에 삭제할 상품 선택하면 해당 idx 가져오고

            val alertDialog = CustomAlertDialog(requireContext(), R.layout.dialog_delete_product)
            val dialog = alertDialog.create()

            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog.window?.attributes)
            layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
            layoutParams.gravity = Gravity.CENTER // 중앙으로 정렬
            dialog.window?.attributes = layoutParams
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 이 부분을 수정
            alertDialog.setNegativeButton("취소") { v ->

                dialog.dismiss()
            }

            alertDialog.setPositiveButton("삭제하기") { v ->

                //여기서 삭제api 실행
                dialog.dismiss()
                popupMsg("해당 상품이 삭제되었습니다.")
            }
            dialog.show()

        }


        return binding.root
    }

    fun init(data: ArrayList<ProductData>?){
        val viewManager = LinearLayoutManager(requireContext())
        val viewAdapter = ProductAdapter(data) { idx ->
            navigateToEditFragment(idx)
        }

        binding.rvProduct.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    fun popupMsg(msg : String){
        val newAlertDialog =
            CustomAlertDialog(requireContext(), R.layout.custom_popup_layout)
        newAlertDialog.setDialogContent(msg)
        val newDialog = newAlertDialog.create()

        val layoutParams = WindowManager.LayoutParams()
       // layoutParams.copyFrom(dialog.window?.attributes)
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

    private fun navigateToEditFragment(idx: Int) {
        val newFragment = ManagingProductFragment()
        val index = datas[idx].experienceGiftId //해당 id의 데이터에 접근해야함
        val bundle = Bundle()
        newFragment.arguments = bundle
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, newFragment)
            .addToBackStack("fragment")
            .commit()

//        val fragment = parentFragmentManager.findFragmentByTag("fragment")
//        if (fragment != null) {
//            Toast.makeText(requireContext(), "있어..", Toast.LENGTH_SHORT).show()
//        } else {
//            Toast.makeText(requireContext(), "없어..", Toast.LENGTH_SHORT).show()
//        }
    }

    private fun RetrofitGetCall(){
        AdminExperienceService().adminExperienceGift { responseState, resultData ->
            when (responseState) {
                RESPONSE_STATE.OKAY -> {
                    Log.d("retrofit", "updateReservation")
                    init(resultData)
                    if (resultData != null) {
                        datas = resultData
                    }
                }

                RESPONSE_STATE.FAIL -> {
                    Log.d("retrofit", "api 호출 에러")
                }
            }
        }
    }


    private fun navigateToNewFragment() {
        val newFragment = ManagingProductFragment()
        val bundle = Bundle()
        newFragment.arguments = bundle
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, newFragment)
            .addToBackStack("fragment")
            .commit()
    }

    override fun onResume() {
        super.onResume()
        val supportActionBar = (requireActivity() as AppCompatActivity).supportActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}