package com.arch.search.presentation.detail


import com.arch.search.base.OnBackPressedListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.lifecycle.Observer
import com.arch.search.R
import com.arch.search.base.BaseFragment
import com.arch.search.databinding.LayoutDetailBinding
import com.arch.search.entity.Search
import com.arch.search.entity.SearchEntity
import com.arch.search.viewmodel.DetailViewModel
import kotlinx.android.synthetic.main.layout_detail.*
import org.koin.androidx.viewmodel.ext.android.getViewModel


class DetailFragment : BaseFragment<LayoutDetailBinding>(), OnBackPressedListener {
    override val layoutResourceId: Int = R.layout.layout_detail


    private var mUrl: String = ""
    private var mIsLocalMode = false
    private var mSearch : Search? = null
    private var mSearchEntity : SearchEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mIsLocalMode = it.getBoolean(ARG_LOCAL)

            mUrl = it.getString(ARG_URL) ?: ""
            if(mIsLocalMode){
                mSearchEntity = it.getParcelable<SearchEntity>(ARG_SEARCH_ENTITY)
            }else{
                mSearch = it.getParcelable<Search>(ARG_SEARCH)
            }

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
        val detailViewModel: DetailViewModel = getViewModel()
        viewDataBinding.detail = detailViewModel
        viewDataBinding.lifecycleOwner = this

        initWebView()

        liveData(detailViewModel)
        if (mUrl.isEmpty()) {
            Toast.makeText(context, resources.getString(R.string.network_check), Toast.LENGTH_SHORT)
                .show()
            activity?.finish()

        } else {
            initUrl(mUrl, detailViewModel)
        }
    }

    private fun initWebView() {
        webview.webViewClient = WebViewClient()
        webview.webChromeClient = WebChromeClient()
        webview.settings.useWideViewPort = true
        webview.settings.javaScriptEnabled = true
        webview.settings.domStorageEnabled = true
        webview.settings.databaseEnabled = true
    }


    private fun initUrl(url: String, detailViewModel: DetailViewModel) {
        detailViewModel.updateUrl(url)
    }


    private fun liveData(
        detailViewModel: DetailViewModel
    ) {
        if(mIsLocalMode){
            mSearchEntity?.let {
                detailViewModel.updateLikeImage(true)
            }?:detailViewModel.updateLikeImage(false)
        }else{
            mSearch?.searchEntity?.let {
                detailViewModel.updateLikeImage(true)
            }?:detailViewModel.updateLikeImage(false)
        }

        detailViewModel.clickedLike.observe(this, Observer {
            if(mIsLocalMode){
                detailViewModel.updateFavorite(mSearchEntity)
            }else{
                detailViewModel.updateFavorite(mSearch)
            }

        })
    }


    override fun onStart() {
        super.onStart()
    }


    companion object {
        private val ARG_URL = "ARG_URL"
        private val ARG_SEARCH = "ARG_SEARCH"
        private val ARG_LOCAL = "ARG_LOCAL"
        private val ARG_SEARCH_ENTITY = "ARG_SEARCH_ENTITY"


        val TAG = "DetailFragment"

        fun newInstance(url: String, search: Search?): DetailFragment {
            val fragment = DetailFragment()
            val args = Bundle()
            args.putString(ARG_URL, url)
            args.putBoolean(ARG_LOCAL, false)
            search?.let {
                args.putParcelable(ARG_SEARCH, it)
            }

            fragment.arguments = args
            return fragment
        }

        fun newInstance(url: String,  searchEntity: SearchEntity?): DetailFragment {
            val fragment = DetailFragment()
            val args = Bundle()
            args.putString(ARG_URL, url)
            args.putBoolean(ARG_LOCAL, true)
            searchEntity?.let {
                args.putParcelable(ARG_SEARCH_ENTITY, it)
            }

            fragment.arguments = args
            return fragment
        }
    }

    override fun onBackPressed(): Boolean {
        return if (webview.canGoBack()) {
            webview.goBack()
            true
        } else {
            false
        }
    }
}
