package com.decorator1889.instruments.adapters

import android.view.LayoutInflater
import android.view.ScaleGestureDetector
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.decorator1889.instruments.databinding.ViewGalleryBinding
import com.decorator1889.instruments.databinding.ViewInstrumentsBinding
import com.decorator1889.instruments.models.Instruments
import com.decorator1889.instruments.util.glide

class InstrumentsGalleryAdapter(
    private val onClickLeft:() -> Unit = {},
    private val onClickRight:() -> Unit = {}
): ListAdapter<Instruments, InstrumentsGalleryAdapter.GalleryViewHolder>(DetailCatalogDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        return GalleryViewHolder.getViewHolder(parent)
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        holder.bind(getItem(position), onClickLeft, onClickRight)
    }

    class GalleryViewHolder(
        private val binding: ViewGalleryBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Instruments, onClickLeft: () -> Unit, onClickRight: () -> Unit) {
            binding.run {
                image.glide(item.image)
                left.setOnClickListener {
                    onClickLeft()
                }
                right.setOnClickListener {
                    onClickRight()
                }
                description.text = item.full_text
            }
        }

        companion object {
            fun getViewHolder(parent: ViewGroup): GalleryViewHolder {
                val binding = ViewGalleryBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return GalleryViewHolder(binding)
            }
        }
    }

    class DetailCatalogDiffUtilCallback : DiffUtil.ItemCallback<Instruments>() {
        override fun areItemsTheSame(oldItem: Instruments, newItem: Instruments): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Instruments, newItem: Instruments): Boolean = oldItem == newItem
    }
}