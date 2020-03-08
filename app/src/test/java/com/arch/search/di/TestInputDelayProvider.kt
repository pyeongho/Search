package com.arch.search.di

import com.arch.search.provider.InputDelayProvider


class TestInputDelayProvider : InputDelayProvider {

    override fun delay(): Long {
        return 0L
    }
}
