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
import com.decorator1889.instruments.adapters.TestCategoriesAdapter
import com.decorator1889.instruments.databinding.FragmentTestCategoriesBinding
import com.decorator1889.instruments.util.DefaultNetworkEventObserver
import com.decorator1889.instruments.util.enums.State
import com.decorator1889.instruments.util.gone
import com.decorator1889.instruments.util.invisible
import com.decorator1889.instruments.util.visible
import com.decorator1889.instruments.viewModels.TestCategoriesViewModel

class TestCategoriesFragment : Fragment() {

    private lateinit var binding: FragmentTestCategoriesBinding
    private val testCategoriesViewModel: TestCategoriesViewModel by viewModels()
    private lateinit var onTypesEvent: DefaultNetworkEventObserver
    private val args: TestCategoriesFragmentArgs by navArgs()

    private val testCategoriesAdapter by lazy {
        TestCategoriesAdapter(onClickSelector = onClickSelector)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentTestCategoriesBinding.inflate(inflater, container, false).apply {
        binding = this
        initializeObservers()
        setObservers()
        setAdapter()
        setListeners()
    }.root

    private fun setListeners() {
        binding.run {
            swipeRefresh.setColorSchemeColors(
                ContextCompat.getColor(requireContext(), R.color.blue_5B67CA)
            )
            swipeRefresh.setOnRefreshListener {
                testCategoriesViewModel.loadTypesTestCategories()
            }
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            start.setOnClickListener {
                testCategoriesViewModel.typesForTest.value?.let {
                    val typesCategories = it.joinToString(",")
                    testCategoriesViewModel.select = true
                    findNavController().navigate(
                        TestCategoriesFragmentDirections.actionTestCategoriesFragmentToTestFragment(
                            typesCategories = typesCategories,
                            level = args.level
                        )
                    )
                }
            }
        }
    }

    private fun setAdapter() {
        binding.recycler.adapter = testCategoriesAdapter
    }

    private fun setObservers() {
        testCategoriesViewModel.run {
            typesResultEvent.observe(viewLifecycleOwner, onTypesEvent)
        }
    }

    override fun onStart() {
        super.onStart()
        if (testCategoriesViewModel.select) {
            testCategoriesViewModel.clearListCategoriesForTest()
        }
        checkIsLoadData()
        (activity as MainActivity).hideBottomNavigationView()
    }

    private fun checkIsLoadData() {
        testCategoriesViewModel.run {
            if (typesResultEvent.value?.peekContent() != State.SUCCESS) loadTypesTestCategories()
            else loadAdapter()
        }
    }

    private fun initializeObservers() {
        onTypesEvent = DefaultNetworkEventObserver(
            anchorView = binding.root,
            doOnLoading = {
                hideTypes()
            },
            doOnSuccess = {
                loadAdapter()
                showTypes()
            }
        )
    }

    private fun loadAdapter() {
        testCategoriesViewModel.types.value?.let { types ->
            testCategoriesAdapter.submitList(types)
        }
    }

    private fun hideTypes() {
        binding.run {
            swipeRefresh.isRefreshing = true
            recycler.alpha = 0f
            recycler.gone()
        }
    }

    private fun showTypes() {
        binding.run {
            recycler.visible()
            swipeRefresh.isRefreshing = false
            recycler.animate().alpha(1f)
        }
    }

    private val onClickSelector: (String, Boolean) -> Unit = { name, isChecked ->
        testCategoriesViewModel.run {
            if (isChecked) {
                addListCategoriesForTest(name)
            } else {
                removeListCategoriesForTest(name)
            }
            checkForTestEmpty(typesForTest.value)
        }
    }

    private fun checkForTestEmpty(value: MutableList<String>?) {
        binding.run {
            if (value?.isEmpty() == true) {
                binding.start.invisible()
            } else {
                binding.start.visible()
            }
        }
    }
}