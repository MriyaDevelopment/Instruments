package com.decorator1889.instruments.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.decorator1889.instruments.R
import com.decorator1889.instruments.databinding.ViewCatalogBinding
import com.decorator1889.instruments.models.Catalog
import com.decorator1889.instruments.util.getInstrumentsBgr
import com.decorator1889.instruments.util.getInstrumentsIcon
import com.decorator1889.instruments.util.str

class CatalogAdapter(
    private val onClickCatalog: (type: String, name: String) -> Unit = { type: String, name: String -> }
): ListAdapter<Catalog, CatalogAdapter.CatalogViewHolder>(CatalogDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogViewHolder {
        return CatalogViewHolder.getViewHolder(parent)
    }

    override fun onBindViewHolder(holder: CatalogViewHolder, position: Int) {
        holder.bind(getItem(position), onClickCatalog)
    }

    class CatalogViewHolder(
        private val binding: ViewCatalogBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Catalog, onClickCatalog: (type: String, name: String) -> Unit) {
            binding.run {
                name.text = item.name
                bgr.setImageResource(getInstrumentsBgr(item.name))
                icon.setImageResource(getInstrumentsIcon(item.name))
                count.text = str(R.string.mainCount, item.count)
                if (item.lock) arrowLock.setImageResource(R.drawable.ic_arrow_catalog)
                else arrowLock.setImageResource(R.drawable.ic_lock_catalog)
                root.setOnClickListener {
                    if (!item.lock) return@setOnClickListener
                    onClickCatalog(item.type, item.name)
                }
            }
        }

        companion object {
            fun getViewHolder(parent: ViewGroup): CatalogViewHolder {
                val binding = ViewCatalogBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return CatalogViewHolder(binding)
            }
        }
    }

    class CatalogDiffUtilCallback : DiffUtil.ItemCallback<Catalog>() {
        override fun areItemsTheSame(oldItem: Catalog, newItem: Catalog): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Catalog, newItem: Catalog): Boolean = oldItem == newItem
    }
}