package com.arch.search.viewmodel


import com.arch.search.base.DisposableViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arch.search.domain.LocalFavoriteSearch
import com.arch.search.entity.SearchEntity
import com.arch.search.presentation.main.local.LocalListAdapter
import com.arch.search.provider.SchedulersProvider
import com.arch.search.util.log


class LocalViewModel(
    private val localFavoriteSearch: LocalFavoriteSearch,
    private val schedulers: SchedulersProvider
) : DisposableViewModel() {

    private val _adapter = MutableLiveData<LocalListAdapter>()
    val adapter: LiveData<LocalListAdapter> get() = _adapter

    private val _showDetail = MutableLiveData<SearchEntity>()
    val showDetail: LiveData<SearchEntity> get() = _showDetail
    var searchLiveData: LiveData<List<SearchEntity>> = localFavoriteSearch.getAllObservable()

    init {
        initAdapter(_adapter)
    }

    private fun initAdapter(adapterArgs: MutableLiveData<LocalListAdapter>) {
        adapterArgs.value =
            LocalListAdapter (
                {
                    deleteSearchEntity(it)
                },
                {
                    _showDetail.value = it
                }
            )
    }

    private fun deleteSearchEntity(searchEntity: SearchEntity) {
        localFavoriteSearch
            .getSearchEntity(searchEntity.slug)
            .flatMap { localFavoriteSearch.delete(it) }
            .observeOn(schedulers.ui())
            .subscribe(
                {
                    log.l()
                },
                {
                    log.e(it.message)

                }
            ).addDisposableEx()

    }

    fun updateList(items: List<SearchEntity>) {
        log.d(items.size.toString())
        _adapter.value?.addAll(items)

    }

    fun search(query: String) {
        log.d(query)
        _adapter.value?.filter(query)
    }
}

