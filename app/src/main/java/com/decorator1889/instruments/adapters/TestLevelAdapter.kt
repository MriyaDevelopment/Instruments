package com.decorator1889.instruments.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.decorator1889.instruments.R
import com.decorator1889.instruments.databinding.ViewDetailCatalogBinding
import com.decorator1889.instruments.databinding.ViewTestLevelBinding
import com.decorator1889.instruments.models.DetailCatalog
import com.decorator1889.instruments.models.TestLevel
import com.decorator1889.instruments.util.*

class TestLevelAdapter(
    private val onClickTestCategory: (String) -> Unit = {}
) : ListAdapter<TestLevel, TestLevelAdapter.TestLevelViewHolder>(TestLevelDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestLevelViewHolder {
        return TestLevelViewHolder.getViewHolder(parent)
    }

    override fun onBindViewHolder(holder: TestLevelViewHolder, position: Int) {
        holder.bind(getItem(position), onClickTestCategory)
    }

    class TestLevelViewHolder(
        private val binding: ViewTestLevelBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TestLevel, onClickTestCategory: (String) -> Unit) {
            binding.run {
                icon.setImageResource(getLevelIcon(item.name))
                bgr.setImageResource(getLevelBgr(item.name))
                nameTest.text = item.name
                count.text = str(R.string.testLevelQuestion, item.count)
                run.setTextColor(ContextCompat.getColor(root.context, getColorLevel(item.name)))
                run.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, getArrowTestCategory(item.name), 0);
                root.setOnClickListener {
                    onClickTestCategory(item.name)
                }
            }
        }

        companion object {
            fun getViewHolder(parent: ViewGroup): TestLevelViewHolder {
                val binding = ViewTestLevelBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return TestLevelViewHolder(binding)
            }
        }
    }

    class TestLevelDiffUtilCallback : DiffUtil.ItemCallback<TestLevel>() {
        override fun areItemsTheSame(oldItem: TestLevel, newItem: TestLevel): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: TestLevel, newItem: TestLevel): Boolean =
            oldItem == newItem
    }
}