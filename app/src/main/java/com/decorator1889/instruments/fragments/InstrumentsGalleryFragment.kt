package com.decorator1889.instruments.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import androidx.viewpager2.widget.ViewPager2
import com.decorator1889.instruments.MainActivity
import com.decorator1889.instruments.adapters.InstrumentsGalleryAdapter
import com.decorator1889.instruments.databinding.FragmentInstrumentsGalleryBinding
import com.decorator1889.instruments.util.Constants
import com.decorator1889.instruments.viewModels.GalleryViewModel
import com.google.android.material.progressindicator.LinearProgressIndicator

class InstrumentsGalleryFragment : Fragment() {

    private lateinit var binding: FragmentInstrumentsGalleryBinding
    private val galleryViewModel: GalleryViewModel by activityViewModels()

    private val arg: InstrumentsGalleryFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentInstrumentsGalleryBinding.inflate(inflater, container, false).apply {
        val transition =
            context?.let { TransitionInflater.from(it).inflateTransition(android.R.transition.move) }
        sharedElementEnterTransition = transition
        sharedElementReturnTransition = transition
        binding = this
        loadGalleryAdapter()
        setListeners()
        Log.d(Constants.INSTRUMENTS_GALLERY_TAG, "InstrumentsGalleryFragment created")
    }.root

    override fun onStart() {
        super.onStart()
        (activity as MainActivity).hideBottomNavigationView()
    }

    private fun setListeners() {
        binding.run {
            toolbar.setNavigationOnClickListener  {
                findNavController().popBackStack()
            }
            toolbar.title = arg.title
        }
    }

    private fun loadGalleryAdapter() {
        binding.run {
            galleryViewModel.galleryImageList.value?.let { galleryList ->
                val galleryAdapter = InstrumentsGalleryAdapter(onClickLeft = onClickLeft, onClickRight = onClickRight)
                viewPager.adapter = galleryAdapter
                viewPager.offscreenPageLimit = 3
                galleryAdapter.submitList(galleryList)
                viewPager.setCurrentItem(arg.position, false)
            }
        }
    }

    private val onClickLeft:() -> Unit = {
        binding.run {
            val currentItem = viewPager.currentItem
            if (currentItem == 0) {
                findNavController().popBackStack()
            }
            viewPager.setCurrentItem(currentItem - 1, true)
        }
    }

    private val onClickRight:() -> Unit = {
        binding.run {
            val totalItems = binding.viewPager.adapter?.itemCount
            val currentItem = viewPager.currentItem
            if (currentItem + 1 == totalItems) {
                findNavController().popBackStack()
            }
            viewPager.setCurrentItem(currentItem + 1, true)
        }
    }
}