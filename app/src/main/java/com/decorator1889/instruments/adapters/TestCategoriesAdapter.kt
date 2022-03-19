package com.decorator1889.instruments.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.decorator1889.instruments.databinding.ViewStartBinding
import com.decorator1889.instruments.databinding.ViewTestCategoriesBinding
import com.decorator1889.instruments.models.Start
import com.decorator1889.instruments.models.Types
import com.decorator1889.instruments.util.getNameMiniCategories
import com.decorator1889.instruments.util.invisible
import com.decorator1889.instruments.util.visible

sealed class TestCategoriesItem(val itemId: Long) {
    data class TestCategoriesWrap(val types: Types) : TestCategoriesItem(types.id)
    data class StartButton(val start: Start) : TestCategoriesItem(-1)
}

class TestCategoriesAdapter(
    private val onClickSelector: (String, Boolean) -> Unit = { name: String, isChecked -> },
    private val onClickStart: () -> Unit = {}
) : ListAdapter<TestCategoriesItem, RecyclerView.ViewHolder>(TestCategoriesDiffUtilCallback()) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is TestCategoriesItem.TestCategoriesWrap -> TYPE_CATEGORIES.hashCode()
            is TestCategoriesItem.StartButton -> TYPE_BUTTON.hashCode()
            else -> {
                throw Exception("Invalid type categories")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_CATEGORIES.hashCode() -> TestCategoriesViewHolder.getViewHolder(parent)
            TYPE_BUTTON.hashCode() -> StartTestViewHolder.getViewHolder(parent)
            else -> throw Exception("Not found view holder type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is TestCategoriesItem.TestCategoriesWrap -> {
                (holder as TestCategoriesViewHolder).bind(item.types, onClickSelector)
            }
            is TestCategoriesItem.StartButton -> {
                (holder as StartTestViewHolder).bind(item.start, onClickStart)
            }
        }
    }

    class TestCategoriesViewHolder(
        private val binding: ViewTestCategoriesBinding
    ) : RecyclerView.ViewHolder(binding.root) {

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

    class StartTestViewHolder(
        private val binding: ViewStartBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Start, onClickStart: () -> Unit) {
            binding.run {
                start.setOnClickListener {
                    onClickStart()
                }
                if (item.start) {
                    start.visible()
                } else {
                    start.invisible()
                }
            }
        }

        companion object {
            fun getViewHolder(parent: ViewGroup): StartTestViewHolder {
                val binding = ViewStartBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return StartTestViewHolder(binding)
            }
        }
    }

    class TestCategoriesDiffUtilCallback : DiffUtil.ItemCallback<TestCategoriesItem>() {
        override fun areItemsTheSame(
            oldItem: TestCategoriesItem,
            newItem: TestCategoriesItem
        ): Boolean = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: TestCategoriesItem,
            newItem: TestCategoriesItem
        ): Boolean = oldItem.itemId == newItem.itemId
    }

    companion object {
        const val TYPE_CATEGORIES = 1
        const val TYPE_BUTTON = 2
    }
}