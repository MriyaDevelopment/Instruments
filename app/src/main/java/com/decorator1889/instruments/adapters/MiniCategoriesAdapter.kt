package com.decorator1889.instruments.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.decorator1889.instruments.databinding.ViewMiniCategoriesBinding
import com.decorator1889.instruments.models.MiniCategory

class MiniCategoriesAdapter: ListAdapter<MiniCategory, MiniCategoriesAdapter.MiniCategoryViewHolder>(MiniCategoryDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MiniCategoryViewHolder {
        return MiniCategoryViewHolder.getViewHolder(parent)
    }

    override fun onBindViewHolder(holder: MiniCategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MiniCategoryViewHolder(
        private val binding: ViewMiniCategoriesBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MiniCategory) {
            binding.run {
                name.text = item.name
            }
        }

        companion object {
            fun getViewHolder(parent: ViewGroup): MiniCategoryViewHolder {
                val binding = ViewMiniCategoriesBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return MiniCategoryViewHolder(binding)
            }
        }
    }

    class MiniCategoryDiffUtilCallback : DiffUtil.ItemCallback<MiniCategory>() {
        override fun areItemsTheSame(oldItem: MiniCategory, newItem: MiniCategory): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: MiniCategory, newItem: MiniCategory): Boolean = oldItem == newItem
    }
}