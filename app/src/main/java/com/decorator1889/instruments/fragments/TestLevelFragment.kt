package com.decorator1889.instruments.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.decorator1889.instruments.R
import com.decorator1889.instruments.adapters.TestLevelAdapter
import com.decorator1889.instruments.databinding.FragmentTestCategoryBinding
import com.decorator1889.instruments.databinding.FragmentTestLevelBinding
import com.decorator1889.instruments.models.getTestLevel

class TestLevelFragment : Fragment() {

    private lateinit var binding: FragmentTestLevelBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentTestLevelBinding.inflate(inflater, container, false).apply {
        binding = this
        setListeners()
        checkIsLoadData()
    }.root

    private fun checkIsLoadData() {
        loadAdapter()
    }

    private fun loadAdapter() {
        val adapter = TestLevelAdapter(onClickTestCategory = onClickTestCategory)
        binding.recycler.adapter = adapter
        adapter.submitList(getTestLevel())
    }

    private fun setListeners() {
        binding.run {
            swipeRefresh.setColorSchemeColors(
                ContextCompat.getColor(requireContext(), R.color.blue_5B67CA)
            )
        }
    }

    private val onClickTestCategory:(String) -> Unit = {

    }
}