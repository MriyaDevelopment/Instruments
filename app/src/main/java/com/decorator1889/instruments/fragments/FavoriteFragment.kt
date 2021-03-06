package com.decorator1889.instruments.fragments

import android.os.Bundle
import android.util.Log
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
import com.decorator1889.instruments.util.*
import com.decorator1889.instruments.util.enums.State
import com.decorator1889.instruments.viewModels.FavoritesViewModel
import com.decorator1889.instruments.viewModels.GalleryViewModel

class FavoriteFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteBinding
    private val favoritesViewModel: FavoritesViewModel by viewModels()
    private val galleryViewModel: GalleryViewModel by activityViewModels()
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
        Log.d(Constants.FAVORITE_TAG, "FavoriteFragment created")
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
        }
    }

    private fun setObservers() {
        favoritesViewModel.run {
            favoritesResultEvent.observe(viewLifecycleOwner, onFavoriteEvent)
            errorLikeEvent.observe(viewLifecycleOwner, OneTimeEvent.Observer {
                loadFavorites()
            })
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
            },
            doOnError = {
                favoritesViewModel.loadFavorites()
            },
            doOnFailure = {
                favoritesViewModel.loadFavorites()
            }
        )
    }

    private fun hideFavorites() {
        binding.run {
            swipeRefresh.isRefreshing = true
            recycler.alpha = 0f
            emptyView.gone()
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
        (activity as MainActivity).run {
            checkBnvMenuItem(R.id.favorite)
            showBottomNavigationView()
        }
    }

    private fun checkIsLoadData() {
        favoritesViewModel.run {
            if (favoritesResultEvent.value?.peekContent() != State.SUCCESS) loadFavorites()
            else loadAdapter()
        }
    }

    private fun loadAdapter() {
        val data = mutableListOf<InstrumentsItem>()
        favoritesViewModel.favorites.value?.let { listFavorites ->
          if (listFavorites.isEmpty()) {
              binding.run {
                  emptyView.visible()
                  recycler.gone()
              }
              return@let
          }
            data.addAll(listFavorites.map { detailCatalog ->
                InstrumentsItem.InstrumentsWrap(detailCatalog)
            })
            galleryViewModel.setInstrumentsGalleryList(listFavorites)
        }
        favoritesAdapter.submitList(data)
    }

    private val onClickDeleteLike: (Instruments) -> Unit = { item ->
        favoritesViewModel.removeLike(instrument_id = item.id, is_surgery = item.is_surgery)
        val newFavoritesList: MutableList<Instruments>? = favoritesViewModel.favorites.value?.map { instruments ->
            Instruments(
                id = instruments.id,
                title = instruments.title,
                type = instruments.type,
                image = instruments.image,
                full_text = instruments.full_text,
                is_liked = instruments.is_liked,
                is_surgery = instruments.is_surgery
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
        const val FAVORITES = "??????????????????"
    }
}