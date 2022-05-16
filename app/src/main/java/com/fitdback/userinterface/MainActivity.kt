package com.fitdback.userinterface


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.fitdback.posedetection.R
import com.fitdback.userinterface.fragment.HomeFragment
import com.fitdback.userinterface.fragment.MyPageFragment
import com.fitdback.userinterface.fragment.MyTownFragment
import com.fitdback.userinterface.fragment.StatisticFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val vp: ViewPager2 by lazy { findViewById(R.id.vp_) }   // TODO:빨간줄 무시할 것!!! 고치면 실행 안됨

    private val bn: BottomNavigationView by lazy { findViewById(R.id.bn_) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        vp.apply {
            adapter = ViewPagerAdapter(this@MainActivity)
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    bn.selectedItemId = when (position) {
                        0 ->R.id.menu_home
                        1 ->R.id.menu_mytown
                        2 ->R.id.menu_stat
                        else -> R.id.menu_mypage
                    }
                }
            })
        }

        bn.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_home -> vp.currentItem = 0
                R.id.menu_mytown -> vp.currentItem = 1
                R.id.menu_stat -> vp.currentItem = 2
                else -> vp.currentItem = 3
            }
            true
        }
    }

    inner class ViewPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount() = 4

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> HomeFragment()
                1 -> MyTownFragment()
                2 -> StatisticFragment()
                else -> MyPageFragment()
            }
        }
    }



    override fun onBackPressed() {
        startActivity(Intent(this,LoginActivity::class.java))
        finish()
    }


}