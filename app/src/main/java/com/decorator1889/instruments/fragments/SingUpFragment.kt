package com.decorator1889.instruments.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.decorator1889.instruments.R
import com.decorator1889.instruments.databinding.FragmentSignInBinding
import com.decorator1889.instruments.databinding.FragmentSingUpBinding

class SingUpFragment : Fragment() {

    private lateinit var binding: FragmentSingUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentSingUpBinding.inflate(inflater, container, false).apply {
        binding = this
        setListeners()
    }.root

    private fun setListeners() {
        binding.run {
            signIn.setOnClickListener {
                findNavController().navigate(SingUpFragmentDirections.actionSingUpFragmentToSignInFragment())
            }
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }
}