package com.shall_we.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.shall_we.admin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar : Toolbar = binding.tbMain
        setSupportActionBar(toolbar)

        getSupportActionBar()?.setDisplayShowTitleEnabled(false); // 기본 타이틀 사용 안함

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_btn) // 뒤로가기 버튼 이미지 설정
        supportActionBar?.setTitle("")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                //toolbar의 back키 눌렀을 때 동작
                // 액티비티 이동
//                Log.d("toolbar","툴바 뒤로가기 버튼 클릭")
                if (supportFragmentManager.backStackEntryCount > 1) {
                    supportFragmentManager.popBackStackImmediate(null, 0)
                }else{
                    supportFragmentManager.popBackStackImmediate(null, 0)
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)

                }
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }
}