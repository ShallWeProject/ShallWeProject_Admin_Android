package com.shall_we.admin.product

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.shall_we.admin.R
import com.shall_we.admin.databinding.FragmentProductBinding


class ProductFragment : Fragment() {
    private var _binding: FragmentProductBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductBinding.inflate(inflater, container, false)

        // Sample data for testing.
        val myDataset = listOf(
            ProductData("[성수]인기 베이킹 클래스", "기념일케이크 사지말고 만들어요"),
            ProductData("[홍대]인기 공예 클래스", "테마가 있는 프라이빗 칵테일 클래스")
            // Add more data here...
        )

        val viewManager = LinearLayoutManager(requireContext())
        val viewAdapter = ProductAdapter(myDataset) { product ->
            navigateToOtherFragment(product)
        }

        binding.rvProduct.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        return binding.root
    }

    private fun navigateToOtherFragment(product : ProductData) {
        val newFragment = ManagingProductFragment()
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
    override fun onResume() {
        super.onResume()
        val supportActionBar = (requireActivity() as AppCompatActivity).supportActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}