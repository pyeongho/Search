package com.arch.search.util


import android.annotation.TargetApi
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.util.AttributeSet
import android.webkit.WebView

class LollipopFixedWebView : WebView {
    constructor(context: Context) : super(
        getFixedContext(
            context
        )
    ) {
    }

    constructor(context: Context, attrs: AttributeSet?) : super(
        getFixedContext(context),
        attrs
    ) {
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(getFixedContext(context), attrs, defStyleAttr) {
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(
        getFixedContext(context),
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
    }

    companion object {
        private fun getFixedContext(context: Context): Context {
            return if (Build.VERSION.SDK_INT >= 21 && Build.VERSION.SDK_INT < 23) {
                    context.createConfigurationContext(Configuration())
            } else context
        }
    }
}