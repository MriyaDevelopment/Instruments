package com.decorator1889.instruments.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.decorator1889.instruments.MainActivity
import com.decorator1889.instruments.R
import com.decorator1889.instruments.adapters.CategoriesAdapter
import com.decorator1889.instruments.databinding.FragmentSubCategoriesBinding
import com.decorator1889.instruments.util.DefaultNetworkEventObserver
import com.decorator1889.instruments.util.GridDecorations
import com.decorator1889.instruments.util.enums.State
import com.decorator1889.instruments.viewModels.SubCategoriesViewModel

class SubCategoriesFragment : Fragment() {

    private lateinit var binding: FragmentSubCategoriesBinding

    private val args: SubCategoriesFragmentArgs by navArgs()
    private val subCategoriesViewModel: SubCategoriesViewModel by viewModels()
    private lateinit var onSubCategoriesEvent: DefaultNetworkEventObserver

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentSubCategoriesBinding.inflate(inflater, container, false).apply {
        binding = this
        setTitleToolbar()
        initializeObservers()
        setObservers()
        itemDecorator()
        setListeners()
    }.root

    private fun setObservers() {
        subCategoriesViewModel.run {
            subCategoriesResultEvent.observe(viewLifecycleOwner, onSubCategoriesEvent)
        }
    }

    private fun initializeObservers() {
        onSubCategoriesEvent = DefaultNetworkEventObserver(
            anchorView = binding.root,
            doOnLoading = {
                hideSubCategories()
            },
            doOnSuccess = {
                loadAdapter()
                showSubCategories()
            },
            doOnError = {
                loadSubCategories()
            },
            doOnFailure = {
                loadSubCategories()
            }
        )
    }

    private fun loadSubCategories() {
        subCategoriesViewModel.loadSubCategories()
    }

    private fun showSubCategories() {
        binding.run {
            swipeRefresh.isRefreshing = false
            recycler.animate().alpha(1f)
        }
    }

    private fun hideSubCategories() {
        binding.run {
            swipeRefresh.isRefreshing = true
            recycler.alpha = 0f
        }
    }

    private fun setListeners() {
        binding.run {
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            swipeRefresh.setColorSchemeColors(
                ContextCompat.getColor(requireContext(), R.color.blue_5B67CA)
            )
        }
    }

    override fun onStart() {
        super.onStart()
        checkIsLoadData()
        (activity as MainActivity).hideBottomNavigationView()
    }

    private fun itemDecorator() {
        binding.recycler.addItemDecoration(
            GridDecorations(
                sideMargins = R.dimen.margin0,
                elementsMargins = R.dimen.margin25,
                horizontalMargins = R.dimen.margin0
            )
        )
    }

    private fun checkIsLoadData() {
        if (subCategoriesViewModel.subCategoriesResultEvent.value?.peekContent() != State.SUCCESS) loadSubCategories()
        else loadAdapter()
    }

    private fun loadAdapter() {
        val adapter = CategoriesAdapter(onClickCatalog = onClickCatalog)
        binding.recycler.adapter = adapter
        subCategoriesViewModel.subCategoriesList.value?.let { categories ->
            adapter.submitList(categories)
        }
    }

    private fun setTitleToolbar() {
        binding.toolbar.title = args.name
    }

    private val onClickCatalog: (type: String, name: String) -> Unit =
        { type: String, name: String ->
            findNavController().navigate(
                SubCategoriesFragmentDirections.actionCategoryFragmentToInstrumentsFragment(
                    section = args.name,
                    subject = name,
                    type = type,
                    surgery = true
                )
            )
        }
}