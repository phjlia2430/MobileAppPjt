package com.example.ch18_image

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.ch18_image.databinding.ActivityNoticeBinding
import com.google.android.material.tabs.TabLayoutMediator

class NoticeActivity : AppCompatActivity() {
    lateinit var binding :ActivityNoticeBinding

    class MyFragmentPagerAdapter(activity:FragmentActivity): FragmentStateAdapter(activity){
        val fragments : List<Fragment>
        init{
            fragments=listOf(OneFragment(),TwoFragment())
        }
        override fun getItemCount(): Int {
            return fragments.size
        }

        override fun createFragment(position: Int): Fragment {
            return fragments[position]
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //setContentView(R.layout.activity_main)
        binding=ActivityNoticeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewpager.adapter = MyFragmentPagerAdapter(this)

        TabLayoutMediator(binding.tabs, binding.viewpager){
                tab, position ->
            tab.text = "TAB${position+1}"
        }.attach()
    }
}