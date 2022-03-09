package com.decorator1889.instruments.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.decorator1889.instruments.R
import com.decorator1889.instruments.databinding.FragmentOnBoardingBinding
import com.decorator1889.instruments.databinding.FragmentSignInBinding

class SignInFragment : Fragment() {

    private lateinit var binding: FragmentSignInBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentSignInBinding.inflate(inflater, container, false).apply {
        binding = this
        setListeners()
    }.root

    private fun setListeners() {
        binding.run {
            signIn.setOnClickListener {

            }
            signUp.setOnClickListener {
                findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToSingUpFragment())
            }
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            forgotPassword.setOnClickListener {
                findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToRecoveryFragment())
            }
        }
    }
}