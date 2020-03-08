package com.arch.search.domain


import androidx.lifecycle.LiveData
import com.arch.search.data.repo.database.SearchDao
import com.arch.search.entity.SearchEntity
import com.arch.search.provider.SchedulersProvider
import com.arch.search.util.log
import io.reactivex.Single

class LocalFavoriteSearch(
    private val repository: SearchDao,
    private val schedulers: SchedulersProvider
) {


    fun getAllObservable(): LiveData<List<SearchEntity>> {
        return repository.getAllObservable()
    }

    fun getAll(): List<SearchEntity> {
        return repository.getAll()
    }

    fun insert(searchEntity: SearchEntity): Single<SearchEntity> {
        return Single.just(searchEntity)
            .subscribeOn(schedulers.io())
            .map { ue ->
                repository.insert(ue)
                ue
            }
    }

    fun getSearchEntity(node_id: String): Single<SearchEntity> {
        return Single.just(node_id)
            .subscribeOn(schedulers.io())
            .map { ni ->
                repository.getWithId(ni)
            }
    }

    fun delete(searchEntity: SearchEntity): Single<SearchEntity> {
        return Single.just(searchEntity)
            .subscribeOn(schedulers.io())
            .map { ue ->
                log.d(ue.slug)
                repository.delete(ue)
                ue
            }
    }

}
