package com.arch.search.presentation.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.arch.search.constant.Constant
import com.arch.search.presentation.main.local.LocalFragment
import com.arch.search.presentation.main.search.SearchFragment

class ScreenSlidePagerAdapter(fa: FragmentManager) : FragmentStatePagerAdapter(fa,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT ) {
    override fun getItem(position: Int): Fragment {
        return if(position ==0 ){
            SearchFragment.newInstance("")
        }else{
            LocalFragment.newInstance("")
        }
    }

    override fun getCount(): Int {
        return Constant.MAX_TAB
    }
}
