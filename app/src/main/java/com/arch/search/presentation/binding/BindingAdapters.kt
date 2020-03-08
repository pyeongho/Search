package com.arch.search.presentation.binding


import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.inputmethod.InputMethodManager
import android.webkit.WebView
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.arch.search.GlideApp
import com.arch.search.R
import com.arch.search.constant.Constant
import com.arch.search.entity.SearchEntity
import com.arch.search.presentation.main.search.SearchListAdapter
import com.arch.search.presentation.main.local.LocalListAdapter


object AddressBindingAdapters {

    @BindingAdapter(value = ["app:detail_url"])
    @JvmStatic
    fun detail_url(v: WebView, url: String?) {
        url?.let {
            v.loadUrl(it)
        }
    }

    @BindingAdapter(value = ["app:search_adapter"])
    @JvmStatic
    fun searchAdapter(v: RecyclerView, adapter: SearchListAdapter?) {
        adapter?.let {
            v.adapter = adapter
        }
    }

    @BindingAdapter(value = ["app:local_adapter"])
    @JvmStatic
    fun localAdapter(v: RecyclerView, adapter: LocalListAdapter?) {
        adapter?.let {
            v.adapter = adapter
        }
    }

    @BindingAdapter(value = ["app:add_scroll_listener"])
    @JvmStatic
    fun addScrollListener(v: RecyclerView, listener: RecyclerView.OnScrollListener?) {
        listener?.let { scr ->
            v.addOnScrollListener(scr)
        }
    }

    @BindingAdapter("app:src_link")
    @JvmStatic
    fun setImageLink(view: ImageView, imageUri: String?) {
        imageUri?.let {
            if (imageUri.isNotEmpty()) {
                GlideApp.with(view.context)
                    .load(imageUri)
                    .placeholder(R.drawable.loading)
                    .thumbnail(0.1f)
                    .into(view)
            } else {
                GlideApp.with(view.context)
                    .load(R.drawable.no_cover_thumb)
                    .into(view)
            }
        }
    }

    @BindingAdapter("app:src")
    @JvmStatic
    fun setImage(view: ImageView, resource: Int?) {
        resource?.let {
            GlideApp.with(view.context)
                .load(resource)
                .into(view)
        }
    }

    private fun getDrawable(context: Context, id: Int): Drawable {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            context.resources.getDrawable(id, context.theme)
        } else {
            context.resources.getDrawable(id);
        }

    }

    @BindingAdapter("app:favorite")
    @JvmStatic
    fun setImageLink(view: ImageView, searchEntity: SearchEntity?) {
        searchEntity?.let {
            view.setImageDrawable(getDrawable(view.context, R.drawable.like))
        } ?: view.setImageDrawable(getDrawable(view.context, R.drawable.unlike))
    }

    @BindingAdapter(value = ["app:search_count"])
    @JvmStatic
    fun showTotalCount(v: TextView, _count: String?) {
        _count?.let { count ->
            val result = if (count == Constant.MAGIC_SEARCH) {
                v.context.resources.getString(R.string.result_searching)
            } else {
                "${v.context.resources.getString(R.string.result_search)} $count"
            }
            v.text = result

        }
    }

    @BindingAdapter(value = ["app:show_keyboard"])
    @JvmStatic
    fun showKeyboard(v: EditText, show: Boolean?) {
        show?.let {
            if (show) {
                v.requestFocus()
                try {
                    val imm = v.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm?.showSoftInput(v, 0)
                } catch (e: Exception) {
                }
            }
        }
    }

}
