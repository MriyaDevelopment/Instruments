package com.decorator1889.instruments.adapters

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.decorator1889.instruments.App
import com.decorator1889.instruments.R
import com.decorator1889.instruments.databinding.ViewMiniCategoriesBinding
import com.decorator1889.instruments.util.getColor25MiniCategories
import com.decorator1889.instruments.util.getColorMiniCategories
import com.decorator1889.instruments.util.getLevelBgr
import com.decorator1889.instruments.util.getNameMiniCategories

class MiniCategoriesAdapter(
): ListAdapter<String, MiniCategoriesAdapter.MiniCategoryViewHolder>(MiniCategoryDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MiniCategoryViewHolder {
        return MiniCategoryViewHolder.getViewHolder(parent)
    }

    override fun onBindViewHolder(holder: MiniCategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MiniCategoryViewHolder(
        private val binding: ViewMiniCategoriesBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String) {
            binding.run {
                name.text = getNameMiniCategories(item)
                container.background = ContextCompat.getDrawable(root.context, getColor25MiniCategories(item))
                name.setTextColor(getColorMiniCategories(item))
                if (name.length() > 15) {
                    name.setTextSize(
                        TypedValue.COMPLEX_UNIT_PX, App.getInstance().resources.getDimension(
                            R.dimen.size11))
                }
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

    class MiniCategoryDiffUtilCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem
        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem
    }
}