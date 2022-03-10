package com.decorator1889.instruments.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.decorator1889.instruments.MainActivity
import com.decorator1889.instruments.R
import com.decorator1889.instruments.adapters.CatalogAdapter
import com.decorator1889.instruments.databinding.FragmentCategoryBinding
import com.decorator1889.instruments.databinding.FragmentMainBinding
import com.decorator1889.instruments.models.getCatalogCategoryList
import com.decorator1889.instruments.util.GridDecorations

class CategoryFragment : Fragment() {

    private lateinit var binding: FragmentCategoryBinding

    private val args: CategoryFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentCategoryBinding.inflate(inflater, container, false).apply {
        binding = this
        setTitleToolbar()
        itemDecorator()
        checkIsLoadData()
        setListeners()
    }.root

    private fun setListeners() {
        binding.run {
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    override fun onStart() {
        super.onStart()
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
        loadAdapter()
    }

    private fun loadAdapter() {
        val adapter = CatalogAdapter(onClickCatalog = onClickCatalog)
        binding.recycler.adapter = adapter
        adapter.submitList(getCatalogCategoryList())
    }

    private fun setTitleToolbar() {
        binding.toolbar.title = args.name
    }

    private val onClickCatalog:(type: String, name: String) -> Unit = { type: String, name: String ->
        findNavController().navigate(CategoryFragmentDirections.actionCategoryFragmentToDetailCatalogFragment(subject = args.name, section = name))
    }
}