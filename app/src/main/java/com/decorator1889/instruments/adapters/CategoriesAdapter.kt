package com.decorator1889.instruments.adapters

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.decorator1889.instruments.App
import com.decorator1889.instruments.R
import com.decorator1889.instruments.databinding.ViewCategoriesBinding
import com.decorator1889.instruments.models.Categories
import com.decorator1889.instruments.util.getInstrumentsBgr
import com.decorator1889.instruments.util.getInstrumentsIcon
import com.decorator1889.instruments.util.str

class CategoriesAdapter(
    private val onClickCatalog: (type: String, name: String) -> Unit = { type: String, name: String -> }
): ListAdapter<Categories, CategoriesAdapter.CategoriesViewHolder>(CatalogDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        return CategoriesViewHolder.getViewHolder(parent)
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        holder.bind(getItem(position), onClickCatalog)
    }

    class CategoriesViewHolder(
        private val binding: ViewCategoriesBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Categories, onClickCatalog: (type: String, name: String) -> Unit) {
            binding.run {
                name.text = item.name
                bgr.setImageResource(getInstrumentsBgr(item.type))
                icon.setImageResource(getInstrumentsIcon(item.type))
                if (item.name.length > 15) {
                    name.setTextSize(TypedValue.COMPLEX_UNIT_PX, App.getInstance().resources.getDimension(R.dimen.size11))
                }
                count.text = str(R.string.mainCount, item.number_of_questions)
                if (item.lock) arrowLock.setImageResource(R.drawable.ic_arrow_catalog)
                else arrowLock.setImageResource(R.drawable.ic_lock_catalog)
                root.setOnClickListener {
                    if (!item.lock) return@setOnClickListener
                    onClickCatalog(item.type, item.name)
                }
            }
        }

        companion object {
            fun getViewHolder(parent: ViewGroup): CategoriesViewHolder {
                val binding = ViewCategoriesBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return CategoriesViewHolder(binding)
            }
        }
    }

    class CatalogDiffUtilCallback : DiffUtil.ItemCallback<Categories>() {
        override fun areItemsTheSame(oldItem: Categories, newItem: Categories): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Categories, newItem: Categories): Boolean = oldItem == newItem
    }
}