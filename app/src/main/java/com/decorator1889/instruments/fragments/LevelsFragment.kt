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
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.decorator1889.instruments.MainActivity
import com.decorator1889.instruments.R
import com.decorator1889.instruments.adapters.LevelsAdapter
import com.decorator1889.instruments.databinding.FragmentLevelsBinding
import com.decorator1889.instruments.util.Constants
import com.decorator1889.instruments.util.DefaultNetworkEventObserver
import com.decorator1889.instruments.util.enums.State
import com.decorator1889.instruments.util.gone
import com.decorator1889.instruments.util.visible
import com.decorator1889.instruments.viewModels.LevelsViewModel

class LevelsFragment : Fragment() {

    private lateinit var binding: FragmentLevelsBinding
    private val levelsViewModel: LevelsViewModel by activityViewModels()
    private lateinit var onLevelsEvent: DefaultNetworkEventObserver

    private val levelsAdapter by lazy {
        LevelsAdapter(onClickTestCategory = onClickTestCategory)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentLevelsBinding.inflate(inflater, container, false).apply {
        binding = this
        initializeObservers()
        setObservers()
        setAdapter()
        setListeners()
        Log.d(Constants.LEVELS_TAG, "LevelsFragment created")
    }.root

    private fun setAdapter() {
        binding.recycler.adapter = levelsAdapter
    }

    private fun setObservers() {
        levelsViewModel.run {
            levelsResultEvent.observe(viewLifecycleOwner, onLevelsEvent)
        }
    }

    private fun initializeObservers() {
        onLevelsEvent = DefaultNetworkEventObserver(
            anchorView = binding.root,
            doOnLoading = {
                hideLevels()
            },
            doOnSuccess = {
                loadAdapter()
                showLevels()
            },
            doOnError = {
                levelsViewModel.loadLevels()
            },
            doOnFailure = {
                levelsViewModel.loadLevels()
            }
        )
    }

    private fun showLevels() {
        binding.run {
            recycler.visible()
            swipeRefresh.isRefreshing = false
            containerTitle.animate().alpha(1f)
            recycler.animate().alpha(1f)
        }
    }

    private fun hideLevels() {
        binding.run {
            swipeRefresh.isRefreshing = true
            containerTitle.alpha = 0f
            recycler.alpha = 0f
            recycler.gone()
        }
    }

    override fun onStart() {
        super.onStart()
        checkIsLoadData()
        (activity as MainActivity).run {
            checkBnvMenuItem(R.id.test)
            showBottomNavigationView()
        }
    }

    private fun checkIsLoadData() {
        levelsViewModel.run {
            if (levelsResultEvent.value?.peekContent() != State.SUCCESS) loadLevels()
            else loadAdapter()
        }
    }

    private fun loadAdapter() {
        levelsViewModel.levels.value?.let { levels ->
            levelsAdapter.submitList(levels)
        }
    }

    private fun setListeners() {
        binding.run {
            swipeRefresh.setColorSchemeColors(
                ContextCompat.getColor(requireContext(), R.color.blue_5B67CA)
            )
            swipeRefresh.setOnRefreshListener {
                levelsViewModel.loadLevels()
            }
        }
    }

    private val onClickTestCategory: (Long) -> Unit = { level ->
        findNavController().navigate(LevelsFragmentDirections.actionLevelsFragmentToTestCategoriesFragment(level = level))
    }
}