package com.arch.search.presentation.main.search


import android.app.AlertDialog
import com.arch.search.base.OnBackPressedListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.arch.search.R
import com.arch.search.act.DetailActivity
import com.arch.search.base.BaseFragment
import com.arch.search.databinding.LayoutSearchsBinding
import com.arch.search.util.log
import com.arch.search.viewmodel.SearchViewModel
import kotlinx.android.synthetic.main.layout_searchs.*
import org.koin.androidx.viewmodel.ext.android.getSharedViewModel


class SearchFragment : BaseFragment<LayoutSearchsBinding>(), OnBackPressedListener {
    override val layoutResourceId: Int = R.layout.layout_searchs


    private var mArg: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mArg = it.getString(ARG_PARAM1) ?: ""
        }


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }



    override fun onResume() {
        super.onResume()
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val searchViewModel :SearchViewModel= getSharedViewModel()
        viewDataBinding.searchViewModel = searchViewModel
        viewDataBinding.lifecycleOwner = this

        liveData(searchViewModel)
    }


    private fun liveData(searchViewModel: SearchViewModel) {
        searchViewModel.searchQuery.observe(this, Observer { startQuery ->
            startQuery?.let {
                searchViewModel.searchImages(startQuery)
            }
        })


        searchViewModel.searchLiveData.observe(this, Observer { items ->
            items?.let {
                searchViewModel.favoriteUpdate(items)
            }
        })

        searchViewModel.showErrorToast.observe(this, Observer {
            context?.let { ctx ->
                Toast.makeText(ctx,it , Toast.LENGTH_LONG).show()
            }
        })

        searchViewModel.showErrorDialog.observe(this, Observer { resourceID ->
            resourceID?.let {
                showAlertDialog(it)
            }

        })

        searchViewModel.showSearchCount.observe(this, Observer { count ->
            count?.let {
                val searched = String.format(resources.getString(R.string.searching_count), it)
                searchStatus.text = searched
            }
        })
        searchViewModel.showSearchingStatus.observe(this, Observer { status ->
            status?.let {
                searchStatus.text = resources.getString(status)
            }
        })

        searchViewModel.showLoading.observe(this, Observer { show ->
            if(show){
                showProgress()
            }else{
                dismissProgress()
            }
        })

        searchViewModel.showDetail.observe(this, Observer {  data ->

            if(data.first.isNotEmpty()){
                DetailActivity.startActivity(context, data.first,data.second)
            }
        })
    }


    private fun showAlertDialog(messageResource : Int){
        val builder = AlertDialog.Builder(context)
        builder.setMessage(resources.getString(messageResource))
            .setCancelable(false)
            .setPositiveButton(resources.getString(android.R.string.ok)) { dialog, id ->
                log.l()
            }
        val alert = builder.create()
        alert.show()
    }

    override fun onStart() {
        super.onStart()
    }



    companion object {
        private val ARG_PARAM1 = "param1"


        val TAG = "SearchFragment"

        fun newInstance(arg: String): SearchFragment {
            val fragment = SearchFragment()
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
