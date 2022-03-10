package com.decorator1889.instruments.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.decorator1889.instruments.R
import com.decorator1889.instruments.adapters.DetailCatalogAdapter
import com.decorator1889.instruments.databinding.FragmentCategoryBinding
import com.decorator1889.instruments.databinding.FragmentDetailCatalogBinding
import com.decorator1889.instruments.models.getDetailCatalog
import com.decorator1889.instruments.util.GridDecorations
import com.decorator1889.instruments.util.str

class DetailCatalogFragment : Fragment() {

    private lateinit var binding: FragmentDetailCatalogBinding

    private val args: DetailCatalogFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentDetailCatalogBinding.inflate(inflater, container, false).apply {
        binding = this
        setTitleToolbar()
        checkIsLoadData()
        setListeners()
    }.root

    private fun setListeners() {
        binding.run {
            swipeRefresh.setColorSchemeColors(
                ContextCompat.getColor(requireContext(), R.color.blue_5B67CA)
            )
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun checkIsLoadData() {
        loadAdapter()
    }

    private fun loadAdapter() {
        val adapter = DetailCatalogAdapter()
        binding.recycler.adapter = adapter
        adapter.submitList(getDetailCatalog())
    }

    private fun setTitleToolbar() {
        binding.run {
            toolbar.title = str(R.string.detailCatalogTitle, args.subject)
            toolbar.subtitle = str(R.string.detailCatalogSubtitle, args.section)
        }
    }
}