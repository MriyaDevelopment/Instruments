package com.decorator1889.instruments.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.decorator1889.instruments.App
import com.decorator1889.instruments.MainActivity
import com.decorator1889.instruments.R
import com.decorator1889.instruments.adapters.CategoriesAdapter
import com.decorator1889.instruments.databinding.FragmentMainBinding
import com.decorator1889.instruments.util.*
import com.decorator1889.instruments.util.enums.State
import com.decorator1889.instruments.util.enums.TypesCategories
import com.decorator1889.instruments.viewModels.MainViewModel
import com.decorator1889.instruments.viewModels.ResultViewModel
import java.lang.reflect.Type

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var onCategoriesEvent: DefaultNetworkEventObserver
    private lateinit var onProfileEvent: DefaultNetworkEventObserver
    private lateinit var onMainPageEvent: DefaultNetworkEventObserver

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentMainBinding.inflate(inflater, container, false).apply {
        binding = this
        initializeObservers()
        setObservers()
        itemDecorator()
        setListeners()
        Log.d(Constants.MAIN_TAG, "MainFragment created")
    }.root

    private fun initializeObservers() {
        onCategoriesEvent = DefaultNetworkEventObserver(
            anchorView = binding.root,
            doOnSuccess = {
                loadCategoriesAdapter()
            }
        )
        onProfileEvent = DefaultNetworkEventObserver(
            anchorView = binding.root,
            doOnSuccess = {
                bindProfileData()
            }
        )
        onMainPageEvent = DefaultNetworkEventObserver(
            anchorView = binding.root,
            doOnLoading = {
                hideCategoriesMain()
            },
            doOnSuccess = {
                showCategoriesMain()
            },
        )
    }

    private fun bindProfileData() {
        mainViewModel.profile.value?.let { profile ->
            if (profile.isEmpty()) {
                return
            }
            val user = profile[0]
            binding.name.text = str(R.string.mainName, user.name)
        }
    }

    private fun hideCategoriesMain() {
        binding.run {
            swipeRefresh.isRefreshing = true
            containerTitle.alpha = 0f
            recycler.alpha = 0f
            recycler.gone()
        }
    }

    private fun showCategoriesMain() {
        binding.run {
            recycler.visible()
            swipeRefresh.isRefreshing = false
            containerTitle.animate().alpha(1f)
            recycler.animate().alpha(1f)
        }
    }

    private fun setObservers() {
        mainViewModel.run {
            categoriesResultEvent.observe(viewLifecycleOwner, onCategoriesEvent)
            profileResultEvent.observe(viewLifecycleOwner, onProfileEvent)
            mainPageLoading.observe(viewLifecycleOwner, onMainPageEvent)
        }
    }

    private fun itemDecorator() {
        binding.recycler.addItemDecoration(
            GridDecorations(
                sideMargins = R.dimen.margin0,
                elementsMargins = R.dimen.margin16,
                horizontalMargins = R.dimen.margin16
            )
        )
    }

    override fun onStart() {
        super.onStart()
        checkIsLoadData()
        (activity as MainActivity).run {
            visibleBottomNav()
            showBottomNavigationView()
            checkBnvMenuItem(R.id.home)
        }
    }

    private fun checkIsLoadData() {
        mainViewModel.run {
            if (mainPageLoading.value?.peekContent() != State.SUCCESS) loadMainData()
            else {
                bindProfileData()
                loadCategoriesAdapter()
            }
        }
    }

    private fun loadCategoriesAdapter() {
        binding.run {
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
                mainViewModel.loadMainData()
            }
        }
    }

    private val onClickCatalog: (type: String, name: String) -> Unit = { type, name ->
        if (type == TypesCategories.SURGERY.types) {
            findNavController().navigate(
                MainFragmentDirections.actionMainFragmentToSubCategoriesFragment(
                    type,
                    name
                )
            )
        } else {
            findNavController().navigate(
                MainFragmentDirections.actionMainFragmentToInstrumentsFragment(
                    section = name,
                    subject = "",
                    type = type,
                    surgery = false
                )
            )
        }
    }
}