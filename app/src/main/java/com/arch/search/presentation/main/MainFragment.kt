package com.arch.search.presentation.main


import com.arch.search.base.OnBackPressedListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.arch.search.R
import com.arch.search.base.BaseFragment
import com.arch.search.constant.Constant
import com.arch.search.databinding.LayoutMainBinding
import com.arch.search.viewmodel.LocalViewModel
import com.arch.search.viewmodel.MainViewModel
import com.arch.search.viewmodel.SearchViewModel
import kotlinx.android.synthetic.main.layout_main.*
import org.koin.androidx.viewmodel.ext.android.getSharedViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel


class MainFragment : BaseFragment<LayoutMainBinding>(), OnBackPressedListener {
    override val layoutResourceId: Int = R.layout.layout_main


    private var mArg: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mArg = it.getString(ARG_PARAM1) ?: ""
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    override fun onResume() {
        super.onResume()

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mainViewModel: MainViewModel = getViewModel()
        val searchViewModel : SearchViewModel = getSharedViewModel()
        val localViewModel: LocalViewModel = getSharedViewModel()
        viewDataBinding.mainViewModel = mainViewModel
        viewDataBinding.lifecycleOwner = this

        initPager(searchViewModel)

        liveData(mainViewModel, searchViewModel, localViewModel)
    }

    private fun initPager(searchViewModel: SearchViewModel) {
        activity?.let { act ->
            pager.adapter = ScreenSlidePagerAdapter(act.supportFragmentManager)
        }

        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                searchViewModel.favoriteUpdateLock(position == Constant.TAB_API)
            }
        })
        searchViewModel.favoriteUpdateLock(true)

        tab_layout.setupWithViewPager(pager)
        if (tab_layout.tabCount >= Constant.MAX_TAB) {
            tab_layout.getTabAt(Constant.TAB_API)?.text = resources.getString(R.string.tab_api)
            tab_layout.getTabAt(Constant.TAB_LOCAL)?.text = resources.getString(R.string.tab_local)
        }
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }
    private fun liveData(
        mainViewModel: MainViewModel,
        searchViewModel: SearchViewModel?,
        localViewModel: LocalViewModel
    ) {

        mainViewModel.searchQuery.observe(this, Observer { query ->
            query?.let {
                if (pager.currentItem == Constant.TAB_API) {
                    searchViewModel?.searchImages(query)
                } else {
                    localViewModel.search(query)
                }
            }
        })
    }


    override fun onStart() {
        super.onStart()
    }


    companion object {
        private val ARG_PARAM1 = "param1"


        val TAG = "MainFragment"

        fun newInstance(arg: String): MainFragment {
            val fragment = MainFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, arg)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onBackPressed(): Boolean {
        return false
    }
}
