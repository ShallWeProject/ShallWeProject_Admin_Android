package com.shall_we.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.shall_we.admin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private var backPressCount = 0
    private val DOUBLE_BACK_PRESS_INTERVAL = 2000 // milliseconds

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

    override fun onBackPressed() {
        Log.d("back",supportFragmentManager.backStackEntryCount.toString())
        if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStackImmediate()
        }
        else if(supportFragmentManager.backStackEntryCount == 1) {
            supportFragmentManager.popBackStackImmediate()
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }
        else {
            Log.d("back",supportFragmentManager.backStackEntryCount.toString())
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            if (backPressCount == 2) {
                super.onBackPressed()
            } else {
                backPressCount++
                Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()
                Handler(Looper.getMainLooper()).postDelayed({
                    backPressCount = 0
                }, DOUBLE_BACK_PRESS_INTERVAL.toLong())
            }
        }
    }
}