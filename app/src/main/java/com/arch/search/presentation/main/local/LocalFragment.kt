package com.arch.search.presentation.main.local


import com.arch.search.base.OnBackPressedListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.arch.search.R
import com.arch.search.act.DetailActivity
import com.arch.search.base.BaseFragment
import com.arch.search.databinding.LayoutLocalsBinding
import com.arch.search.util.LinearLayoutManagerWrapper
import com.arch.search.viewmodel.LocalViewModel
import kotlinx.android.synthetic.main.layout_locals.*
import org.koin.androidx.viewmodel.ext.android.getSharedViewModel


class LocalFragment : BaseFragment<LayoutLocalsBinding>(), OnBackPressedListener {
    override val layoutResourceId: Int = R.layout.layout_locals


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
        recyclerView.layoutManager =
            LinearLayoutManagerWrapper(context!!, LinearLayoutManager.VERTICAL, false)
        val localViewModel: LocalViewModel = getSharedViewModel()
        viewDataBinding.localViewModel = localViewModel
        viewDataBinding.lifecycleOwner = this

        liveData(localViewModel)
    }


    private fun liveData(localViewModel: LocalViewModel) {
        localViewModel.searchLiveData.observe(this, Observer { items ->
            items?.let {
                localViewModel.updateList(items)
            }
        })

        localViewModel.showDetail.observe(this, Observer { search ->
            search?.let {
                DetailActivity.startActivityFromLocal(context, it.url, it )
            }
        })
    }


    override fun onStart() {
        super.onStart()
    }


    companion object {
        private val ARG_PARAM1 = "param1"


        val TAG = "LocalFragment"

        fun newInstance(arg: String): LocalFragment {
            val fragment = LocalFragment()
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
