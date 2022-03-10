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
import com.decorator1889.instruments.models.Catalog
import com.decorator1889.instruments.models.DetailCatalog
import com.decorator1889.instruments.util.glide

class DetailCatalogAdapter: ListAdapter<DetailCatalog, DetailCatalogAdapter.DetailCatalogViewHolder>(DetailCatalogDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailCatalogViewHolder {
        return DetailCatalogViewHolder.getViewHolder(parent)
    }

    override fun onBindViewHolder(holder: DetailCatalogViewHolder, position: Int) {
        holder.bind(getItem(position))
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

    class DetailCatalogDiffUtilCallback : DiffUtil.ItemCallback<DetailCatalog>() {
        override fun areItemsTheSame(oldItem: DetailCatalog, newItem: DetailCatalog): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: DetailCatalog, newItem: DetailCatalog): Boolean = oldItem == newItem
    }
}