package com.decorator1889.instruments.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.decorator1889.instruments.MainActivity
import com.decorator1889.instruments.R
import com.decorator1889.instruments.adapters.CatalogAdapter
import com.decorator1889.instruments.databinding.FragmentMainBinding
import com.decorator1889.instruments.models.getCatalogList
import com.decorator1889.instruments.util.GridDecorations
import com.decorator1889.instruments.util.str

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentMainBinding.inflate(inflater, container, false).apply {
        binding = this
        checkIsLoadData()
        itemDecorator()
        setListeners()
    }.root

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
        (activity as MainActivity).showBottomNavigationView()
    }

    private fun checkIsLoadData() {
        bindData()
    }

    private fun bindData() {
        binding.run {
            name.text = str(R.string.mainName, "Ксения")
            val adapter = CatalogAdapter(onClickCatalog = onClickCatalog)
            recycler.adapter = adapter
            adapter.submitList(getCatalogList())
        }
    }

    private fun setListeners() {
        binding.run {
            swipeRefresh.setColorSchemeColors(
                ContextCompat.getColor(requireContext(), R.color.blue_5B67CA)
            )
        }
    }

    private val onClickCatalog: (type: String, name: String) -> Unit = { type, name ->
        findNavController().navigate(MainFragmentDirections.actionMainFragmentToCategoryFragment(type = type, name = name))
    }
}