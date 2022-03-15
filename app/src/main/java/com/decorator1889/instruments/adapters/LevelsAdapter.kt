package com.decorator1889.instruments.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.decorator1889.instruments.R
import com.decorator1889.instruments.databinding.ViewTestLevelBinding
import com.decorator1889.instruments.models.Levels
import com.decorator1889.instruments.util.*

class LevelsAdapter(
    private val onClickTestCategory: (String) -> Unit = {}
) : ListAdapter<Levels, LevelsAdapter.TestLevelViewHolder>(TestLevelDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestLevelViewHolder {
        return TestLevelViewHolder.getViewHolder(parent)
    }

    override fun onBindViewHolder(holder: TestLevelViewHolder, position: Int) {
        holder.bind(getItem(position), onClickTestCategory)
    }

    class TestLevelViewHolder(
        private val binding: ViewTestLevelBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Levels, onClickTestCategory: (String) -> Unit) {
            binding.run {
                icon.setImageResource(getLevelIcon(item.name))
                containerTest.background = ContextCompat.getDrawable(root.context, getLevelBgr(item.name))
                nameTest.text = item.name
                count.text = str(R.string.testLevelQuestion, item.number_of_questions)
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

    class TestLevelDiffUtilCallback : DiffUtil.ItemCallback<Levels>() {
        override fun areItemsTheSame(oldItem: Levels, newItem: Levels): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Levels, newItem: Levels): Boolean = oldItem == newItem
    }
}