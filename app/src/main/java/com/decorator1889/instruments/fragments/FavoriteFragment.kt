package com.decorator1889.instruments.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.decorator1889.instruments.databinding.FragmentFavoriteBinding

class FavoriteFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentFavoriteBinding.inflate(inflater, container, false).apply {
        binding = this
        checkIsLoadData()
    }.root

    private fun checkIsLoadData() {
        loadAdapter()
    }

    private fun loadAdapter() {
//        val data = mutableListOf<DetailCatalogItem>()
//        val adapter = DetailCatalogAdapter(onClickFavorite = onClickFavorite)
//        binding.recycler.adapter = adapter
//        data.add(DetailCatalogItem.DetailCatalogFavoriteButton)
//        getDetailCatalog().let {
//            data.addAll(it.map { detailCatalog ->
//                DetailCatalogItem.DetailCatalogWrap(detailCatalog)
//            })
//        }
//        adapter.submitList(data)
    }

    private val onClickFavorite: () -> Unit = {

    }
}