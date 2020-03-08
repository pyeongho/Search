package com.arch.search.act

import com.arch.search.base.OnBackPressedListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.arch.search.R
import com.arch.search.presentation.main.MainFragment
import com.arch.search.util.log

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basic)
        initFragment()
    }

    private var onBackPressedListener: OnBackPressedListener? = null
    private fun initFragment(): Unit {

        val fragment = MainFragment.newInstance("")
        val transaction = this.supportFragmentManager.beginTransaction()
        transaction.add(R.id.full, fragment, MainFragment.TAG)
        transaction.commit()
        onBackPressedListener = fragment
    }

    override fun onBackPressed() {
        if (onBackPressedListener?.onBackPressed() == true) {
            log.l()
        } else {
            super.onBackPressed()
        }
    }
}
