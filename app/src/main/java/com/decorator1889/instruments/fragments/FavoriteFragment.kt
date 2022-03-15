package com.decorator1889.instruments.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.decorator1889.instruments.MainActivity
import com.decorator1889.instruments.R
import com.decorator1889.instruments.adapters.InstrumentsAdapter
import com.decorator1889.instruments.adapters.InstrumentsItem
import com.decorator1889.instruments.databinding.FragmentFavoriteBinding
import com.decorator1889.instruments.models.Instruments
import com.decorator1889.instruments.util.DefaultNetworkEventObserver
import com.decorator1889.instruments.util.enums.State
import com.decorator1889.instruments.util.gone
import com.decorator1889.instruments.util.visible
import com.decorator1889.instruments.viewModels.FavoritesViewModel

class FavoriteFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteBinding
    private val favoritesViewModel: FavoritesViewModel by viewModels()
    private lateinit var onFavoriteEvent: DefaultNetworkEventObserver
    private val favoritesAdapter by lazy {
        InstrumentsAdapter(onClickDeleteLike = onClickDeleteLike, onClickImage = onClickImage, typeInstruments = FAVORITES)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentFavoriteBinding.inflate(inflater, container, false).apply {
        binding = this
        initializeObservers()
        setObservers()
        setAdapter()
        setListeners()
        postponeTransition()
    }.root

    private fun postponeTransition() {
        postponeEnterTransition()
        binding.recycler.post { startPostponedEnterTransition() }
    }

    private fun setListeners() {
        binding.run {
            swipeRefresh.setColorSchemeColors(
                ContextCompat.getColor(requireContext(), R.color.blue_5B67CA)
            )
            swipeRefresh.setOnRefreshListener {
                favoritesViewModel.loadFavorites()
            }
        }
    }

    private fun setAdapter() {
        binding.run {
            recycler.adapter = favoritesAdapter
            recycler.itemAnimator = null
        }
    }

    private fun setObservers() {
        favoritesViewModel.run {
            favoritesResultEvent.observe(viewLifecycleOwner, onFavoriteEvent)
        }
    }

    private fun initializeObservers() {
        onFavoriteEvent = DefaultNetworkEventObserver(
            anchorView = binding.root,
            doOnLoading = {
                hideFavorites()
            },
            doOnSuccess = {
                loadAdapter()
                showFavorites()
            }
        )
    }

    private fun hideFavorites() {
        binding.run {
            swipeRefresh.isRefreshing = true
            recycler.alpha = 0f
            binding.emptyView.gone()
            recycler.gone()
        }
    }

    private fun showFavorites() {
        binding.run {
            recycler.visible()
            recycler.animate().alpha(1f)
            swipeRefresh.isRefreshing = false
        }
    }

    override fun onStart() {
        super.onStart()
        checkIsLoadData()
        (activity as MainActivity).showBottomNavigationView()
    }

    private fun checkIsLoadData() {
        favoritesViewModel.run {
            if (favoritesResultEvent.value?.peekContent() != State.SUCCESS) loadFavorites()
            else loadAdapter()
        }
    }

    private fun loadAdapter() {
        val data = mutableListOf<InstrumentsItem>()
        favoritesViewModel.favorites.value?.let {
          if (it.isEmpty()) {
              binding.emptyView.visible()
              return
          }
            data.add(InstrumentsItem.InstrumentsFavoriteButton)
            data.addAll(it.map { detailCatalog ->
                InstrumentsItem.InstrumentsWrap(detailCatalog)
            })
        }
        favoritesAdapter.submitList(data)
    }

    private val onClickDeleteLike: (Instruments) -> Unit = { item ->
        val newFavoritesList: MutableList<Instruments>? = favoritesViewModel.favorites.value?.map { instruments ->
            Instruments(
                id = instruments.id,
                title = instruments.title,
                type = instruments.type,
                image = instruments.image,
                full_text = instruments.full_text,
                is_liked = instruments.is_liked
            )
        }?.toMutableList()
        newFavoritesList?.remove(item)
        newFavoritesList?.let {
            favoritesViewModel.setNewFavoritesList(it)
        }
        loadAdapter()
    }

    private val onClickImage: (Int, ImageView) -> Unit = { position, imageView ->
        val transitionExtras = FragmentNavigatorExtras(
            imageView to "quick_image_transition",
        )
        findNavController().navigate(
            FavoriteFragmentDirections.actionFavoriteFragmentToInstrumentsGalleryFragment(
                position = position,
                title = FAVORITES
            ), navigatorExtras = transitionExtras
        )
    }

    companion object {
        const val FAVORITES = "Избранное"
    }
}