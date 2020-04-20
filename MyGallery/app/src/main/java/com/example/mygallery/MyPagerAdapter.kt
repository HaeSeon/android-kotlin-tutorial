package com.example.mygallery

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class MyPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    //뷰페이저가 표시할 프래그먼트 목록
    private val items=ArrayList<Fragment>()

    //position 위치에 어떤 프래그먼트를 표시할지 정의
    override fun getItem(position: Int) : Fragment{
        return items[position]
    }

    //아이텐(프래그먼트) 갯수 정의
    override fun getCount() : Int{
        return items.size
    }


    fun updateFragments(items : List<Fragment>){
        Log.d("Fragment Item", items[0].toString())
        this.items.addAll(items)
    }
}