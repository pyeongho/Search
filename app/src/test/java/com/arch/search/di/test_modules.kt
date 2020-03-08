package com.arch.search.di

import com.arch.search.provider.SchedulersProvider
import com.arch.search.data.repo.api.ApiRemote
import com.arch.search.data.repo.database.SearchDao
import com.arch.search.provider.InputDelayProvider
import org.koin.dsl.module

val testModule = module {

    single(override = true) {
        TestDummySearchDaoImpl() as SearchDao
    }

    single(override = true) {
        TestSchedulerProvider() as SchedulersProvider
    }

    single(override = true) {
        TestInputDelayProvider() as InputDelayProvider
    }

    single(override = true) {
        TestDummyApiRemoteImpl() as ApiRemote
    }
}

val test_module = listOf(searchModules, testModule)
