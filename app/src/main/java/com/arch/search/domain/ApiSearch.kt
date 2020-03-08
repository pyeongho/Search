package com.arch.search.domain


import com.arch.search.provider.SchedulersProvider
import com.arch.search.data.repo.api.ApiRemote
import com.arch.search.data.keep.model.req.ReqSearch
import com.arch.search.domain.mapper.ResSearchToSearch
import com.arch.search.entity.ResultSearch
import io.reactivex.Single

class ApiSearch(
    private val repository: ApiRemote,
    private val schedulers: SchedulersProvider
) {


    fun search(search: ReqSearch): Single<ResultSearch> {
        return repository.getResults(search)
            .subscribeOn(schedulers.io())
            .map { ResSearchToSearch(it).result }

    }

}
