package com.arch.search.viewmodel


import android.view.View
import com.arch.search.base.DisposableViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arch.search.R
import com.arch.search.base.SingleLiveEvent
import com.arch.search.domain.LocalFavoriteSearch
import com.arch.search.entity.Search
import com.arch.search.entity.SearchEntity
import com.arch.search.provider.SchedulersProvider
import com.arch.search.util.Parameter
import com.arch.search.util.log


class DetailViewModel(
    private val localFavoriteSearch: LocalFavoriteSearch,
    private val schedulers: SchedulersProvider

) : DisposableViewModel() {

    private val _detailUrl = MutableLiveData<String>()
    val detailUrl: LiveData<String> get() = _detailUrl

    private val _clickedLike = SingleLiveEvent<Parameter>()
    val clickedLike: LiveData<Parameter> get() = _clickedLike


    private val _showLikeImage = SingleLiveEvent<Int>()
    val showLikeImage: LiveData<Int> get() = _showLikeImage


    fun updateUrl(url: String) {
        _detailUrl.value = url
    }

    private var mIsLike = true
    private fun deleteData(slug: String) {
        localFavoriteSearch
            .getSearchEntity(slug)
            .flatMap { localFavoriteSearch.delete(it) }
            .observeOn(schedulers.ui())
            .subscribe(
                {
                    log.l()
                    mIsLike = false
                    _showLikeImage.value = R.drawable.unlike
                },
                {
                    log.e(it.message)
                }
            ).addDisposableEx()
    }

    fun updateFavorite( searchEntity: SearchEntity?) {
        searchEntity?.let {
            if(mIsLike){
                deleteData(searchEntity.slug)
            }else{
                insert(searchEntity)
            }
        } ?: insert(searchEntity)
    }

    fun updateFavorite( search: Search?) {
        search?.searchEntity?.let {
            deleteData(it.slug)
        } ?: insertData(search)

    }

    private fun insert(searchEntity: SearchEntity?) {
        searchEntity?.let {
            localFavoriteSearch
                .insert(searchEntity)
                .map { true }
                .observeOn(schedulers.ui())
                .subscribe(
                    {
                        log.l()
                        mIsLike = true
                        _showLikeImage.value = R.drawable.like
                    },
                    {
                        log.e(it.message)
                    }
                ).addDisposableEx()
        }

    }

    private fun insertData(search: Search?) {
        search?.let {
            val searchEntity = SearchEntity(
                slug = search.slug,
                id = search.id,
                title = search.title,
                image_url = search.image_url,
                url = search.url,
                rating = search.rating
            )
            insert(searchEntity)
        }
    }

    fun clickedLike(v: View) {
        _clickedLike.call()
    }

    fun updateLikeImage(like: Boolean) {
        mIsLike = like
        if (like) {
            _showLikeImage.value = R.drawable.like
        } else {
            _showLikeImage.value = R.drawable.unlike
        }
    }


}

