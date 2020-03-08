package com.arch.search.di


import com.arch.search.BuildConfig
import com.arch.search.provider.SchedulersProvider
import com.arch.search.data.net.ApiServiceFactory
import com.arch.search.data.repo.api.ApiRemote
import com.arch.search.data.repo.api.ApiRemoteImpl
import com.arch.search.data.repo.database.AppDatabase
import com.arch.search.domain.ApiSearch
import com.arch.search.domain.LocalFavoriteSearch
import com.arch.search.provider.InputDelayProvider
import com.arch.search.viewmodel.DetailViewModel
import com.arch.search.viewmodel.LocalViewModel
import com.arch.search.viewmodel.MainViewModel
import com.arch.search.viewmodel.SearchViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val searchModules: Module = module {

    single { AppSchedulerProvider() as SchedulersProvider }
    single { AppInputDelayProvider() as InputDelayProvider }

    viewModel { SearchViewModel(get(), get(), get(), get()) }
    viewModel { MainViewModel() }
    viewModel { LocalViewModel(get(), get()) }
    viewModel { DetailViewModel(get(),get()) }

    //domain
    factory { ApiSearch(get(), get()) }
    factory { LocalFavoriteSearch(get(), get()) }


    //data
    single { AppDatabase.getInstance(androidContext()).searchDao() }
    single { ApiRemoteImpl(get()) as ApiRemote }
    single {
        ApiServiceFactory.makeApiService(
            BuildConfig.DEBUG,
            BuildConfig.BASE_URL
        )
    }
}

