package com.decorator1889.instruments.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.decorator1889.instruments.MainActivity
import com.decorator1889.instruments.R
import com.decorator1889.instruments.adapters.CategoriesAdapter
import com.decorator1889.instruments.databinding.FragmentMainBinding
import com.decorator1889.instruments.util.*
import com.decorator1889.instruments.util.enums.State
import com.decorator1889.instruments.viewModels.MainViewModel

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var onCategoriesEvent: DefaultNetworkEventObserver

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentMainBinding.inflate(inflater, container, false).apply {
        binding = this
        initializeObservers()
        setObservers()
        itemDecorator()
        setListeners()
    }.root

    private fun initializeObservers() {
        onCategoriesEvent = DefaultNetworkEventObserver(
            anchorView = binding.root,
            doOnLoading = {
                hideCategoriesMain()
            },
            doOnSuccess = {
                loadCategoriesAdapter()
                showCategoriesMain()
            },
            doOnError = {
                mainViewModel.loadCategories()
            },
            doOnFailure = {
                mainViewModel.loadCategories()
            }
        )
    }

    private fun hideCategoriesMain() {
        binding.run {
            swipeRefresh.isRefreshing = true
            containerTitle.alpha = 0f
            recycler.alpha = 0f
        }
    }

    private fun showCategoriesMain() {
        binding.run {
            swipeRefresh.isRefreshing = false
            containerTitle.animate().alpha(1f)
            recycler.animate().alpha(1f)
        }
    }

    private fun setObservers() {
        mainViewModel.run {
            categoriesResultEvent.observe(viewLifecycleOwner, onCategoriesEvent)
        }
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

    override fun onStart() {
        super.onStart()
        checkIsLoadData()
        (activity as MainActivity).showBottomNavigationView()
    }

    private fun checkIsLoadData() {
        mainViewModel.run {
            if (categoriesResultEvent.value?.peekContent() != State.SUCCESS) loadCategories()
            else loadCategoriesAdapter()
        }
    }

    private fun loadCategoriesAdapter() {
        binding.run {
            name.text = str(R.string.mainName, "Гость")
            val adapter = CategoriesAdapter(onClickCatalog = onClickCatalog)
            recycler.adapter = adapter
            mainViewModel.categoriesList.value?.let { categories ->
                adapter.submitList(categories)
            }
        }
    }

    private fun setListeners() {
        binding.run {
            swipeRefresh.setColorSchemeColors(
                ContextCompat.getColor(requireContext(), R.color.blue_5B67CA)
            )
            swipeRefresh.setOnRefreshListener {
                mainViewModel.loadCategories()
            }
            subscribe.setOnClickListener {
                createSnackbar(
                    binding.root,
                    getString(R.string.functionalityIsInDevelopment)
                ).show()
            }
        }
    }

    private val onClickCatalog: (type: String, name: String) -> Unit = { type, name ->
        if (type == surgery) {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToSubCategoriesFragment(type, name))
        } else {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToInstrumentsFragment(section = name, subject = "", type = type, surgery = false))
        }
    }
}