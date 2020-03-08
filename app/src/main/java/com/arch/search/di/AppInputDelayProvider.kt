package com.arch.search.di

import com.arch.search.constant.Constant
import com.arch.search.provider.InputDelayProvider

class AppInputDelayProvider : InputDelayProvider {

    override fun delay(): Long {
        return Constant.INPUT_TIME_DELAY
    }
}
