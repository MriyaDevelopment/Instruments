package com.decorator1889.instruments.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.decorator1889.instruments.databinding.ViewTestCategoriesBinding
import com.decorator1889.instruments.models.Types
import com.decorator1889.instruments.util.getNameMiniCategories

class TestCategoriesAdapter(
    private val onClickSelector:(String, Boolean) -> Unit = { name: String, isChecked -> }
): ListAdapter<Types, TestCategoriesAdapter.TestCategoriesViewHolder>(TestCategoriesDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestCategoriesViewHolder {
        return TestCategoriesViewHolder.getViewHolder(parent)
    }

    override fun onBindViewHolder(holder: TestCategoriesViewHolder, position: Int) {
        holder.bind(getItem(position), onClickSelector)
    }

    class TestCategoriesViewHolder(
        private val binding: ViewTestCategoriesBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Types, onClickSelector: (String, Boolean) -> Unit) {
            binding.run {
                selection.text = getNameMiniCategories(item.name)
                selection.setOnCheckedChangeListener { buttonView, isChecked ->
                    onClickSelector(item.name, isChecked)
                }
            }
        }

        companion object {
            fun getViewHolder(parent: ViewGroup): TestCategoriesViewHolder {
                val binding = ViewTestCategoriesBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return TestCategoriesViewHolder(binding)
            }
        }
    }

    class TestCategoriesDiffUtilCallback : DiffUtil.ItemCallback<Types>() {
        override fun areItemsTheSame(oldItem: Types, newItem: Types): Boolean = oldItem == newItem
        override fun areContentsTheSame(oldItem: Types, newItem: Types): Boolean = oldItem.id == newItem.id
    }

    interface ITestCategoriesCallback {
        fun setClickSelect(name: String, isChecked: Boolean)
    }
}