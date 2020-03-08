package com.arch.search.act

import android.content.Context
import android.content.Intent
import com.arch.search.base.OnBackPressedListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.arch.search.R
import com.arch.search.constant.Constant
import com.arch.search.entity.Search
import com.arch.search.entity.SearchEntity
import com.arch.search.presentation.detail.DetailFragment
import com.arch.search.presentation.main.MainFragment
import com.arch.search.util.log

class DetailActivity : AppCompatActivity() {

    companion object {
        fun startActivity(context: Context?, url :String, search: Search) {
            val itn = Intent(context, DetailActivity::class.java)
            itn.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            itn.putExtra(Constant.IT_URL , url)
            itn.putExtra(Constant.IT_SEARCH , search)
            context?.startActivity(itn)
        }

        fun startActivityFromLocal(context: Context?, url :String, searchEntity: SearchEntity) {
            val itn = Intent(context, DetailActivity::class.java)
            itn.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            itn.putExtra(Constant.IT_URL , url)
            itn.putExtra(Constant.IT_SEARCH_ENTITY , searchEntity)
            context?.startActivity(itn)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basic)
        initFragment()
    }

    private var onBackPressedListener: OnBackPressedListener? = null
    private fun initFragment(): Unit {

        val fragment = if(intent.hasExtra(Constant.IT_SEARCH)){
            DetailFragment.newInstance(
                intent.getStringExtra(Constant.IT_URL)?:"",
                intent.getParcelableExtra<Search>(Constant.IT_SEARCH)
            )
        }else{
            DetailFragment.newInstance(
                intent.getStringExtra(Constant.IT_URL)?:"",
                intent.getParcelableExtra<SearchEntity>(Constant.IT_SEARCH_ENTITY)
            )
        }

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
