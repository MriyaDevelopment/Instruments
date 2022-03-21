package com.decorator1889.instruments.fragments

import android.os.Bundle
import android.util.Log
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
import com.decorator1889.instruments.adapters.InstrumentsItem
import com.decorator1889.instruments.adapters.TestCategoriesAdapter
import com.decorator1889.instruments.adapters.TestCategoriesItem
import com.decorator1889.instruments.databinding.FragmentTestCategoriesBinding
import com.decorator1889.instruments.models.Start
import com.decorator1889.instruments.util.*
import com.decorator1889.instruments.util.enums.State
import com.decorator1889.instruments.viewModels.TestCategoriesViewModel

class TestCategoriesFragment : Fragment() {

    private lateinit var binding: FragmentTestCategoriesBinding
    private val testCategoriesViewModel: TestCategoriesViewModel by viewModels()
    private lateinit var onTypesEvent: DefaultNetworkEventObserver
    private val args: TestCategoriesFragmentArgs by navArgs()

    private val testCategoriesAdapter by lazy {
        TestCategoriesAdapter(onClickSelector = onClickSelector, onClickStart = onClickStart)
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
        Log.d(Constants.TEST_CATEGORIES_TAG, "TestCategoriesFragment created")
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
            },
            doOnError = {
                testCategoriesViewModel.loadTypesTestCategories()
            },
            doOnFailure = {
                testCategoriesViewModel.loadTypesTestCategories()
            }
        )
    }

    private fun loadAdapter() {
        val data = mutableListOf<TestCategoriesItem>()
        testCategoriesViewModel.types.value?.let { typesList ->
            data.addAll(typesList.map { types ->
                TestCategoriesItem.TestCategoriesWrap(types)
            })
            data.add(TestCategoriesItem.StartButton(Start(testCategoriesViewModel.start)))
        }
        testCategoriesAdapter.submitList(data)
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

    private val onClickStart:() -> Unit = {
        testCategoriesViewModel.typesForTest.value?.let {
            val typesCategories = it.joinToString(",")
            testCategoriesViewModel.select = true
            testCategoriesViewModel.start = false
            findNavController().navigate(
                TestCategoriesFragmentDirections.actionTestCategoriesFragmentToTestFragment(
                    typesCategories = typesCategories,
                    level = args.level
                )
            )
        }
    }

    private fun checkForTestEmpty(value: MutableList<String>?) {
        binding.run {
            testCategoriesViewModel.start = value?.isEmpty() != true
            loadAdapter()
        }
    }
}