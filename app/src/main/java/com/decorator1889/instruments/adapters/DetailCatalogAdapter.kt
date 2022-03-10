package com.decorator1889.instruments.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.decorator1889.instruments.R
import com.decorator1889.instruments.databinding.ViewCatalogBinding
import com.decorator1889.instruments.databinding.ViewDetailCatalogBinding
import com.decorator1889.instruments.databinding.ViewDetailFavoriteBinding
import com.decorator1889.instruments.models.Catalog
import com.decorator1889.instruments.models.DetailCatalog
import com.decorator1889.instruments.util.glide

sealed class DetailCatalogItem(val itemId: Long) {
    data class DetailCatalogWrap(val detailCatalog: DetailCatalog) : DetailCatalogItem(detailCatalog.id)
    object DetailCatalogFavoriteButton: DetailCatalogItem(-1)
}
class DetailCatalogAdapter(
    private val onClickFavorite:() -> Unit = {}
): ListAdapter<DetailCatalogItem, RecyclerView.ViewHolder>(DetailCatalogDiffUtilCallback()) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DetailCatalogItem.DetailCatalogWrap -> DETAIL_CATALOG.hashCode()
            is DetailCatalogItem.DetailCatalogFavoriteButton -> DETAIL_FAVORITE.hashCode()
            else -> {
                throw Exception("Invalid type detail catalog")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            DETAIL_CATALOG.hashCode() -> DetailCatalogViewHolder.getViewHolder(parent)
            DETAIL_FAVORITE.hashCode() -> DetailFavoriteViewHolder.getViewHolder(parent)
            else -> throw Exception("Not found view holder type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(val item = getItem(position)) {
            is DetailCatalogItem.DetailCatalogWrap -> {
                (holder as DetailCatalogViewHolder).bind(item.detailCatalog)
            }
            is DetailCatalogItem.DetailCatalogFavoriteButton -> {
                (holder as DetailFavoriteViewHolder).bind(onClickFavorite)
            }
        }
    }

    class DetailCatalogViewHolder(
        private val binding: ViewDetailCatalogBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DetailCatalog) {
            binding.run {
                name.text = item.name
                description.text = item.description
                image.glide(item.image)
                if (item.favorite) {
                    favorite.icon = ContextCompat.getDrawable(root.context, R.drawable.ic_favorite_active)
                } else {
                    favorite.icon = ContextCompat.getDrawable(root.context, R.drawable.ic_favorite_inactive)
                }
                favorite.setOnClickListener {
                    if (item.favorite) {
                        item.favorite = false
                        favorite.icon = ContextCompat.getDrawable(root.context, R.drawable.ic_favorite_inactive)
                    } else {
                        item.favorite = true
                        favorite.icon = ContextCompat.getDrawable(root.context, R.drawable.ic_favorite_active)
                    }
                }
            }
        }

        companion object {
            fun getViewHolder(parent: ViewGroup): DetailCatalogViewHolder {
                val binding = ViewDetailCatalogBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return DetailCatalogViewHolder(binding)
            }
        }
    }

    class DetailFavoriteViewHolder(
        private val binding: ViewDetailFavoriteBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(onClickFavorite: () -> Unit) {
            binding.favorite.setOnClickListener {
                onClickFavorite()
            }
        }

        companion object {
            fun getViewHolder(parent: ViewGroup): DetailFavoriteViewHolder {
                val binding = ViewDetailFavoriteBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return DetailFavoriteViewHolder(binding)
            }
        }
    }

    class DetailCatalogDiffUtilCallback : DiffUtil.ItemCallback<DetailCatalogItem>() {
        override fun areItemsTheSame(oldItem: DetailCatalogItem, newItem: DetailCatalogItem): Boolean = oldItem.itemId == newItem.itemId
        override fun areContentsTheSame(oldItem: DetailCatalogItem, newItem: DetailCatalogItem): Boolean = oldItem == newItem
    }

    companion object {
        const val DETAIL_CATALOG = 1
        const val DETAIL_FAVORITE = 2
    }
}