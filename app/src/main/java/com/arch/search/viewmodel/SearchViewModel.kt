package com.arch.search.viewmodel


import com.arch.search.base.DisposableViewModel
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arch.search.R
import com.arch.search.provider.SchedulersProvider
import com.arch.search.constant.Constant


import com.arch.search.domain.ApiSearch
import com.arch.search.data.keep.model.req.ReqSearch
import com.arch.search.domain.LocalFavoriteSearch
import com.arch.search.entity.Search
import com.arch.search.entity.ResultSearch
import com.arch.search.entity.SearchEntity
import com.arch.search.presentation.main.search.SearchListAdapter
import com.arch.search.provider.InputDelayProvider
import com.arch.search.util.log
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit


class SearchViewModel(
    private val mApiSearch: ApiSearch,
    private val localFavoriteSearch: LocalFavoriteSearch,
    private val schedulers: SchedulersProvider,
    private val inputDelay: InputDelayProvider
) : DisposableViewModel() {

    private val mTriggerSearch: PublishSubject<SearQuery> = PublishSubject.create()

    private val _showKeyboard = MutableLiveData<Boolean>().apply {
        value = true
    }
    val showKeyboard: LiveData<Boolean> get() = _showKeyboard

    private val _showSearchingStatus = MutableLiveData<Int>().apply {
        value = R.string.searching_hint
    }
    val showSearchingStatus: LiveData<Int> get() = _showSearchingStatus

    private val _showErrorToast = MutableLiveData<String>()
    val showErrorToast: LiveData<String> get() = _showErrorToast

    private val _showSearchCount = MutableLiveData<Int>()
    val showSearchCount: LiveData<Int> get() = _showSearchCount

    private val _showDetail = MutableLiveData<Pair<String, Search>>()
    val showDetail: LiveData<Pair<String, Search>> get() = _showDetail

    private val _showErrorDialog = MutableLiveData<Int>()
    val showErrorDialog: LiveData<Int> get() = _showErrorDialog

    private val _showLoading = MutableLiveData<Boolean>()
    val showLoading: LiveData<Boolean> get() = _showLoading

    private val _adapter = MutableLiveData<SearchListAdapter>()
    val adapter: LiveData<SearchListAdapter> get() = _adapter

    private val _searchQuery = MutableLiveData<String>()
    val searchQuery: LiveData<String> get() = _searchQuery


    val searchLiveData: LiveData<List<SearchEntity>> = localFavoriteSearch.getAllObservable()


    private var mLock = false
    private var mLoadingFlag = false
    private var mTotalCount = 0
    private var mSearchQuery = SearQuery("",0)

    init {
        initSearch(mTriggerSearch)

        initAdapter(_adapter)
    }

    private fun getFavoriteListener(adapterArgs: MutableLiveData<SearchListAdapter>) :SearchListAdapter.OnItemClickListener{
        return object : SearchListAdapter.OnItemClickListener {
            override fun onClick(view: View, index: Int) {
                adapterArgs.value?.mDataList?.let { searchs ->
                    if (searchs.size > index) {
                        log.l()
                        favoriteUpdateLock(true)
                        updateFavorite(index, searchs[index], _adapter.value)

                    }
                }
            }
        }
    }

    private fun getDetailListener(adapterArgs: MutableLiveData<SearchListAdapter>) :SearchListAdapter.OnItemClickListener{
        return object : SearchListAdapter.OnItemClickListener {
            override fun onClick(view: View, index: Int) {
                adapterArgs.value?.mDataList?.let { searchs ->
                    if (searchs.size > index) {
                        log.l()
                        favoriteUpdateLock(false)
                        _showDetail.value = Pair(searchs[index].url,searchs[index])

                    }
                }
            }
        }
    }
    private fun initAdapter(adapterArgs: MutableLiveData<SearchListAdapter>) {
        adapterArgs.value =
            SearchListAdapter(ArrayList<Search>(),
                getFavoriteListener(adapterArgs),
                getDetailListener(adapterArgs)
            )
    }

    private fun updateFavorite(
        index: Int,
        search: Search,
        searchListAdapter: SearchListAdapter?
    ) {
        searchListAdapter?.let { ula ->
            search.searchEntity?.let { searchEntity ->
                deleteSearchEntity(searchEntity, index, searchListAdapter)
            } ?: insertSearchEntity(search, index, searchListAdapter)
        }

    }

    private fun insertSearchEntity(
        search: Search,
        index: Int,
        searchListAdapter: SearchListAdapter
    ) {
        localFavoriteSearch
            .insert(
                SearchEntity(
                    slug = search.slug,
                    id = search.id,
                    title = search.title,
                    image_url = search.image_url,
                    url = search.url,
                    rating = search.rating
                )
            )
            .observeOn(schedulers.ui())
            .subscribe(
                {
                    log.l()
                    searchListAdapter.insertSearchEntity(index, it)
                },
                {
                    log.e(it.message)
                    _showErrorToast.value = it.message
                }
            ).addDisposableEx()

    }

    private fun deleteSearchEntity(
        searchEntity: SearchEntity,
        index: Int,
        searchListAdapter: SearchListAdapter
    ) {
        localFavoriteSearch
            .getSearchEntity(searchEntity.slug)
            .flatMap { localFavoriteSearch.delete(it) }
            .observeOn(schedulers.ui())
            .subscribe(
                {
                    log.l()
                    searchListAdapter.deleteSearchEntity(index)

                },
                {
                    log.e(it.message)
                    _showErrorToast.value = it.message
                }
            ).addDisposableEx()

    }

    fun searchImages(query: String) {
        mSearchQuery.query = query
        mSearchQuery.offset = 0
        mTriggerSearch.onNext(mSearchQuery)
    }

    private fun clearList() {
        _showSearchingStatus.value = R.string.searching_hint
        _adapter.value?.clear()
    }

    private fun findLocal(locals: List<SearchEntity>, items: ArrayList<Search>) {
        for (item in items) {
            for (localItem in locals) {
                if (item.slug == localItem.slug) {
                    item.searchEntity = localItem
                    break
                }
            }
        }
    }

    private fun parseResult(result: ResultSearch, isNext: Boolean) {
        when (result) {
            is ResultSearch.ConnectionError -> {
                _showSearchingStatus.value = R.string.connection_error
                _showErrorDialog.value = R.string.connection_error
            }
            is ResultSearch.ApiError -> {
                _showErrorToast.value = "[${result.data.responseCode}] ${result.data.message}"
                clearList()
            }
            is ResultSearch.Success -> {
                mTotalCount = result.data.itemCount
                _showSearchCount.value =result.data.itemCount
                if(mSearchQuery.offset == 0){
                    adapter.value?.initItem(result.data.items)
                }else{
                    adapter.value?.addItems(result.data.items)
                }


            }
            is ResultSearch.CommonError -> {
                _showErrorToast.value = result.toString()
            }
        }
    }

    private fun initSearch(trigger: PublishSubject<SearQuery>) {
        trigger
            .debounce(inputDelay.delay(), TimeUnit.MILLISECONDS)
            .observeOn(schedulers.ui())
            .filter { query ->
                if (query.query.isEmpty()) {
                    clearList()
                    false
                } else {
                    true
                }
            }
            .switchMapSingle { search ->
                _showSearchingStatus.value = R.string.result_searching
                mApiSearch.search(ReqSearch(search.query,search.offset.toString()))
                    .map { result ->
                        if (result is ResultSearch.Success) {
                            findLocal(localFavoriteSearch.getAll(), result.data.items)
                        }
                        result
                    }
            }
            .observeOn(schedulers.ui())
            .subscribe(
                { result ->
                    parseResult(result, false)
                    mLoadingFlag = false
                    _showLoading.value = false
                },
                {
                    log.e(it.message)
                    mLoadingFlag = false
                    _showLoading.value = false
                    _showErrorToast.value = it.message
                }
            ).addDisposableEx()

    }

    fun getScrollListener(): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(rcv: RecyclerView, newState: Int) {
                super.onScrollStateChanged(rcv, newState)
            }

            override fun onScrolled(rcv: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(rcv, dx, dy)

                val hasItemCount = rcv.layoutManager?.itemCount ?: 0
                val lastVisibleItem =
                    (rcv.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()

                if(mTotalCount >hasItemCount &&  !mLoadingFlag && hasItemCount <= (lastVisibleItem + Constant.VISIBLE_THRESHOLD)){
                    mLoadingFlag = true
                    _showLoading.value = true
                    mSearchQuery.offset += Constant.PER_PAGE_COUNT
                    mTriggerSearch.onNext(mSearchQuery)


                }
            }
        }
    }

    fun favoriteUpdate(items: List<SearchEntity>) {
        if (!mLock) {
            log.l()
            adapter.value?.updateFavorite(items)
        }
    }

    fun favoriteUpdateLock(lock: Boolean) {
        mLock = lock
    }




    inner class SearQuery(var query: String , var offset : Int)

}

