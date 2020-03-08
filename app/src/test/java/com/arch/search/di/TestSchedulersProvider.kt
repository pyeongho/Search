package com.arch.search.di

import com.arch.search.provider.SchedulersProvider
import io.reactivex.schedulers.Schedulers


class TestSchedulerProvider : SchedulersProvider {
    override fun io() = Schedulers.trampoline()

    override fun ui() = Schedulers.trampoline()
}
