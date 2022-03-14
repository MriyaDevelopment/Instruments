package com.decorator1889.instruments.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.decorator1889.instruments.R
import com.decorator1889.instruments.databinding.ViewInstrumentsBinding
import com.decorator1889.instruments.models.Instruments
import com.decorator1889.instruments.util.glide

sealed class InstrumentsItem(val itemId: Long) {
    data class InstrumentsWrap(val instruments: Instruments) :InstrumentsItem(instruments.id)
    object InstrumentsFavoriteButton: InstrumentsItem(-1)
}
class InstrumentsAdapter(
    private val onClickFavorite:() -> Unit = {},
    private val onClickImage: (Int, ImageView) -> Unit = { position: Int, imageView: ImageView -> }
): ListAdapter<InstrumentsItem, RecyclerView.ViewHolder>(DetailCatalogDiffUtilCallback()) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is InstrumentsItem.InstrumentsWrap -> TYPE_INSTRUMENTS.hashCode()
            is InstrumentsItem.InstrumentsFavoriteButton -> TYPE_FAVORITE.hashCode()
            else -> {
                throw Exception("Invalid type instruments")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_INSTRUMENTS.hashCode() -> InstrumentsViewHolder.getViewHolder(parent)
            TYPE_FAVORITE.hashCode() -> InstrumentsFavoriteViewHolder.getViewHolder(parent)
            else -> throw Exception("Not found view holder type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(val item = getItem(position)) {
            is InstrumentsItem.InstrumentsWrap -> {
                (holder as InstrumentsViewHolder).bind(item.instruments, onClickImage)
            }
            is InstrumentsItem.InstrumentsFavoriteButton-> {
                (holder as InstrumentsFavoriteViewHolder).bind(onClickFavorite)
            }
        }
    }

    class InstrumentsViewHolder(
        private val binding: ViewInstrumentsBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Instruments, onClickImage: (Int, ImageView) -> Unit) {
            binding.run {
                title.text = item.title
                description.text = item.full_text
                image.transitionName = "transition_quick${item.id}"
                image.glide(item.image)
                image.setOnClickListener {
                    onClickImage(adapterPosition, image)
                }
                if (item.is_liked) favorite.icon = ContextCompat.getDrawable(root.context, R.drawable.ic_favorite_active)
                else favorite.icon = ContextCompat.getDrawable(root.context, R.drawable.ic_favorite_inactive)
                favorite.setOnClickListener {
                    if (item.is_liked) {
                        favorite.icon = ContextCompat.getDrawable(root.context, R.drawable.ic_favorite_inactive)
                    } else {
                        favorite.icon = ContextCompat.getDrawable(root.context, R.drawable.ic_favorite_active)
                    }
                }
            }
        }

        companion object {
            fun getViewHolder(parent: ViewGroup): InstrumentsViewHolder {
                val binding = ViewInstrumentsBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return InstrumentsViewHolder(binding)
            }
        }
    }

    class InstrumentsFavoriteViewHolder(
        private val binding: ViewInstrumentsBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(onClickFavorite: () -> Unit) {
            binding.favorite.setOnClickListener {
                onClickFavorite()
            }
        }

        companion object {
            fun getViewHolder(parent: ViewGroup): InstrumentsFavoriteViewHolder {
                val binding = ViewInstrumentsBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return InstrumentsFavoriteViewHolder(binding)
            }
        }
    }

    class DetailCatalogDiffUtilCallback : DiffUtil.ItemCallback<InstrumentsItem>() {
        override fun areItemsTheSame(oldItem: InstrumentsItem, newItem: InstrumentsItem): Boolean = oldItem.itemId == newItem.itemId
        override fun areContentsTheSame(oldItem: InstrumentsItem, newItem: InstrumentsItem): Boolean = oldItem == newItem
    }

    companion object {
        const val TYPE_INSTRUMENTS = 1
        const val TYPE_FAVORITE = 2
    }
}