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
import androidx.navigation.fragment.navArgs
import com.decorator1889.instruments.MainActivity
import com.decorator1889.instruments.R
import com.decorator1889.instruments.adapters.InstrumentsAdapter
import com.decorator1889.instruments.adapters.InstrumentsItem
import com.decorator1889.instruments.databinding.FragmentInstrumentsBinding
import com.decorator1889.instruments.util.DefaultNetworkEventObserver
import com.decorator1889.instruments.util.enums.State
import com.decorator1889.instruments.util.str
import com.decorator1889.instruments.viewModels.GalleryViewModel
import com.decorator1889.instruments.viewModels.InstrumentsViewModel

class InstrumentsFragment : Fragment() {

    private lateinit var binding: FragmentInstrumentsBinding

    private val args: InstrumentsFragmentArgs by navArgs()
    private val instrumentsViewModel: InstrumentsViewModel by viewModels()
    private val galleryViewModel: GalleryViewModel by activityViewModels()
    private lateinit var onInstrumentsEvent: DefaultNetworkEventObserver

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentInstrumentsBinding.inflate(inflater, container, false).apply {
        binding = this
        initializeObservers()
        setObservers()
        setTitleToolbar()
        setListeners()
        postponeTransition()
    }.root

    private fun postponeTransition() {
        postponeEnterTransition()
        binding.recycler.post { startPostponedEnterTransition() }
    }

    private fun setObservers() {
        instrumentsViewModel.run {
            instrumentsResultEvent.observe(viewLifecycleOwner, onInstrumentsEvent)
        }
    }

    private fun initializeObservers() {
        onInstrumentsEvent = DefaultNetworkEventObserver(
            anchorView = binding.root,
            doOnLoading = {
                hideInstruments()
            },
            doOnSuccess = {
                loadAdapter()
                showInstruments()
            },
            doOnError = {
                loadInstruments()
            },
            doOnFailure = {
                loadInstruments()
            }
        )
    }

    private fun loadInstruments() {
        instrumentsViewModel.run {
            if (args.surgery) loadSurgeryInstruments(args.type)
            else loadInstruments(args.type)
        }
    }

    private fun showInstruments() {
        binding.run {
            swipeRefresh.isRefreshing = false
            recycler.animate().alpha(1f)
        }
    }

    private fun hideInstruments() {
        binding.run {
            swipeRefresh.isRefreshing = true
            recycler.alpha = 0f
        }
    }

    override fun onStart() {
        super.onStart()
        checkIsLoadData()
        (activity as MainActivity).hideBottomNavigationView()
    }

    private fun setListeners() {
        binding.run {
            swipeRefresh.setColorSchemeColors(
                ContextCompat.getColor(requireContext(), R.color.blue_5B67CA)
            )
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            swipeRefresh.setOnRefreshListener {
                loadInstruments()
            }
        }
    }

    private fun checkIsLoadData() {
        if (instrumentsViewModel.instrumentsResultEvent.value?.peekContent() != State.SUCCESS) loadInstruments()
        else loadAdapter()
    }

    private fun loadAdapter() {
        val adapter = InstrumentsAdapter(onClickImage = onClickImage)
        binding.recycler.adapter = adapter
        val data = mutableListOf<InstrumentsItem>()
        instrumentsViewModel.instruments.value?.let { instrumentsList ->
            data.addAll(instrumentsList.map { instruments ->
                InstrumentsItem.InstrumentsWrap(instruments)
            })
            galleryViewModel.setInstrumentsGalleryList(instrumentsList)
        }
        adapter.submitList(data)
    }

    private val onClickImage: (Int, ImageView) -> Unit = { position, imageView ->
        val transitionExtras = FragmentNavigatorExtras(
            imageView to "quick_image_transition",
        )
        findNavController().navigate(InstrumentsFragmentDirections.actionInstrumentsFragmentToInstrumentsGalleryFragment(position = position, title = args.section), navigatorExtras = transitionExtras)
    }

    private fun setTitleToolbar() {
        binding.run {
            toolbar.title = str(R.string.detailCatalogTitle, args.section)
            if (args.subject.isEmpty()) {
                return
            }
            toolbar.subtitle = str(R.string.detailCatalogSubtitle, args.subject)
        }
    }
}