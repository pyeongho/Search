package com.arch.search.base


import androidx.lifecycle.ViewModel
import com.arch.search.util.log

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable



abstract class DisposableViewModel: ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    fun Disposable.addDisposableEx() {
        compositeDisposable.add(this)

    }
    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

}
