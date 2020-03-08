package com.arch.search.base


import android.content.res.Resources
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.arch.search.presentation.dialog.DialogLoading
import com.arch.search.util.log

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


abstract class BaseFragment<T : ViewDataBinding> : Fragment() {

    lateinit var viewDataBinding: T
    abstract val layoutResourceId: Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = DataBindingUtil.inflate(inflater, layoutResourceId, container, false)

        return viewDataBinding.root
    }

    override fun onStart() {
        super.onStart()

    }

    val Float.toPx: Float
        get() = (this * Resources.getSystem().displayMetrics.density)

    val Int.toDp: Int
        get() = (this / Resources.getSystem().displayMetrics.density).toInt()

    val Int.toPx: Int
        get() = (this * Resources.getSystem().displayMetrics.density).toInt()


    fun getUsableHeight(): Int {
        val metrics = DisplayMetrics()
        activity?.let {
            it.windowManager.defaultDisplay.getMetrics(metrics)
        }

        val usableHeight = metrics.heightPixels
        return usableHeight.toDp
    }

    fun getUsableWidth(): Int {
        val metrics = DisplayMetrics()
        activity?.let {
            it.windowManager.defaultDisplay.getMetrics(metrics)
        }
        val usable = metrics.widthPixels
        return usable.toDp
    }


    private var mNetDialog: DialogLoading? = null
    private fun getLoading(): DialogLoading? {
        return mNetDialog?.let { dialog ->
            dialog
        } ?: instanceLoading()
    }

    private fun instanceLoading(): DialogLoading? {
        val loading = activity?.let {
            DialogLoading(it)
        }

        mNetDialog = loading
        return loading
    }

    fun showProgress() {
        try {
            getLoading()?.let {
                it.show()

            }
        } catch (e: Exception) {
            log.e(e.message)
        }

    }


    fun dismissProgress() {
        try {
            getLoading()?.let {
                it.dismiss()
            }
        } catch (e: Exception) {
            log.e(e.message)
        }
    }


    override fun onDestroy() {
        dismissProgress()
        compositeDisposable.clear()
        super.onDestroy()
    }

    private val compositeDisposable = CompositeDisposable()

    fun Disposable.addDisposableEx() {
        log.d(compositeDisposable.size().toString())
        compositeDisposable.add(this)

    }
}
