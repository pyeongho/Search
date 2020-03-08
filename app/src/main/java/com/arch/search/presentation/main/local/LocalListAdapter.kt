package com.arch.search.presentation.main.local

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.arch.search.databinding.ItemLocalBinding
import com.arch.search.entity.SearchEntity
import com.arch.search.util.log
import java.util.*
import kotlin.collections.ArrayList

class LocalListAdapter(
    private var mFavoriteCallback: (search: SearchEntity) -> Unit,
    private var mDetailCallback: (search: SearchEntity) -> Unit
) :
    ListAdapter<SearchEntity, LocalListAdapter.ViewHolder>(SearchEntityListDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemLocalBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    private var mOriginalList = ArrayList<SearchEntity>()
    private var mFindList = ArrayList<SearchEntity>()

    fun addAll(list: List<SearchEntity>) {
        if (mFindList.isEmpty()) {
            submitList(list)
            mOriginalList.clear()
            mOriginalList.addAll(currentList)
        } else {
            mOriginalList.clear()
            mOriginalList.addAll(list)
            processLocalSearchRemove()
        }
    }

    private fun processLocalSearchRemove() {
        val removeList = ArrayList<SearchEntity>()
        for (founded in mFindList) {
            var find = false
            for (original in mOriginalList) {
                if (founded.slug == original.slug) {
                    find = true
                    break
                }
            }
            if (!find) {
                removeList.add(founded)
            }

        }
        for (at in removeList) {
            mFindList.remove(at)
        }
        submitList(mFindList)
        notifyDataSetChanged()
    }

    fun filter(filter: String) {
        log.d(filter)
        val lower = filter.toLowerCase(Locale.getDefault())
        mFindList.clear()
        if (lower.isEmpty()) {
            submitList(mOriginalList)
            notifyDataSetChanged()
        } else {
            for (find in mOriginalList) {
                val title = find.title.toLowerCase(Locale.getDefault())
                if (title.contains(lower)) {
                    mFindList.add(find)
                }
            }
            submitList(mFindList)
            notifyDataSetChanged()

        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mFavoriteCallback,mDetailCallback,  getItem(position))
    }


    class ViewHolder(private val mBinding: ItemLocalBinding) : RecyclerView.ViewHolder(mBinding.root) {
        fun bind(
            favorite: (search: SearchEntity) -> Unit,
            detail: (search: SearchEntity) -> Unit,
                 searchEntity: SearchEntity) {
            mBinding.search = searchEntity
            mBinding.root.setOnClickListener {
                log.d(searchEntity.title)
                detail(searchEntity)
            }
            mBinding.favorite.setOnClickListener {
                log.d(searchEntity.title)
                favorite(searchEntity)
            }

        }
    }
}

private class SearchEntityListDiffCallback : DiffUtil.ItemCallback<SearchEntity>() {
    override fun areItemsTheSame(oldItem: SearchEntity, newItem: SearchEntity): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: SearchEntity, newItem: SearchEntity): Boolean {
        return oldItem.slug == newItem.slug
    }
}
