package com.arch.search.viewmodel


import com.arch.search.base.DisposableViewModel
import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


class MainViewModel(

) : DisposableViewModel() {

    private val _searchQuery = MutableLiveData<String>()
    val searchQuery: LiveData<String> get() = _searchQuery

    fun searchTextChanged(s: Editable?) {
        s?.let {
            try {
                _searchQuery.value = it.toString()
            } catch (e: Exception) {
            }
        }
    }
}

