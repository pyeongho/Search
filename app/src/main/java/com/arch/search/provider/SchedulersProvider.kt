package com.arch.search.provider

import io.reactivex.Scheduler

interface SchedulersProvider {
    fun io(): Scheduler
    fun ui(): Scheduler
}
