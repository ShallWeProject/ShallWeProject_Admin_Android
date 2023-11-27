package com.shall_we.admin.login.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shall_we.admin.R
import com.shall_we.admin.databinding.FragmentShopInfoBinding

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


}