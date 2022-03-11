package com.decorator1889.instruments.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.decorator1889.instruments.R
import com.decorator1889.instruments.databinding.FragmentOnBoardingBinding
import com.decorator1889.instruments.databinding.FragmentRecoveryBinding

class RecoveryFragment : Fragment() {

    private lateinit var binding: FragmentRecoveryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentRecoveryBinding.inflate(inflater, container, false).apply {
        binding = this
        setListeners()
    }.root

    private fun setListeners() {
        binding.run {
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }
}