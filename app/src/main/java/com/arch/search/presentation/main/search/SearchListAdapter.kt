package com.arch.search.presentation.main.search


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arch.search.databinding.ItemSearchBinding
import com.arch.search.entity.SearchEntity
import com.arch.search.entity.Search
import com.arch.search.util.log
import java.lang.Exception

class SearchListAdapter(var mDataList: ArrayList<Search>,
                        private val mFavoriteListener: OnItemClickListener,
                        private val mDetailListener: OnItemClickListener) :
    RecyclerView.Adapter<SearchListAdapter.BindingHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemSearchBinding.inflate(layoutInflater, parent, false)
        return BindingHolder(binding, mFavoriteListener,mDetailListener)
    }

    override fun onBindViewHolder(holder: BindingHolder, position: Int) {
        val data = mDataList[position]
        holder.binding.search = data
    }


    override fun getItemCount(): Int {
        return mDataList.size
    }

    interface OnItemClickListener {
        fun onClick(view: View, index: Int)
    }

    fun initItem(item: ArrayList<Search>) {
        mDataList.clear()
        mDataList.addAll(item)
        notifyAllCatch()
    }

    private fun notifyAllCatch() {
        try {
            notifyDataSetChanged()
        } catch (e: Exception) {
        }
    }

    fun addItems(item: ArrayList<Search>) {
        val start = mDataList.size
        mDataList.addAll(item)
        notifyItemRangeInserted(start, item.size)
    }

    fun clear() {
        mDataList.clear()
        notifyAllCatch()
    }

    fun insertSearchEntity(index: Int, searchEntity: SearchEntity) {
        mDataList[index].searchEntity = searchEntity
        notifyItemChanged(index)
    }

    fun deleteSearchEntity(index: Int) {
        mDataList[index].searchEntity = null
        notifyItemChanged(index)
    }

    fun updateFavorite(localFavorite: List<SearchEntity>) {
        if(localFavorite.isEmpty()){
            for (search in mDataList) {
                search.searchEntity = null
            }
        }else{
            for (search in mDataList) {
                for (local in localFavorite) {
                    search.searchEntity = null
                    if (search.slug == local.slug) {
                        if (search.searchEntity == null) {
                            search.searchEntity = local
                            break
                        }
                    }
                }
            }
        }
        notifyAllCatch()
    }

    class BindingHolder(
        var binding: ItemSearchBinding,
        private val favoriteListener: OnItemClickListener,
        private val detailListener: OnItemClickListener
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.favorite.setOnClickListener { v ->
                val index = adapterPosition
                if (index != RecyclerView.NO_POSITION) { // 데이터 리스트로부터 아이템 데이터 참조.
                    log.d(index.toString())
                    favoriteListener.onClick(v, index)
                }
            }
            binding.root.setOnClickListener { v ->
                val index = adapterPosition
                if (index != RecyclerView.NO_POSITION) { // 데이터 리스트로부터 아이템 데이터 참조.
                    log.d(index.toString())
                    detailListener.onClick(v, index)

                }
            }
        }
    }
}
