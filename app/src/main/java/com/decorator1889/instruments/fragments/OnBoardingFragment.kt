package com.decorator1889.instruments.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.decorator1889.instruments.App
import com.decorator1889.instruments.MainActivity
import com.decorator1889.instruments.R
import com.decorator1889.instruments.databinding.FragmentOnBoardingBinding

class OnBoardingFragment : Fragment() {

    private lateinit var binding: FragmentOnBoardingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentOnBoardingBinding.inflate(inflater, container, false).apply {
        binding = this
        setListeners()
    }.root

    override fun onStart() {
        super.onStart()
        hideBottomNav()
    }

    private fun hideBottomNav() {
        (activity as MainActivity).run {
            hideBottomNavigationView()
            goneBottomNav()
        }
    }

    private fun setListeners() {
        binding.run {
            signIn.setOnClickListener {
                findNavController().navigate(OnBoardingFragmentDirections.actionOnBoardingFragmentToSignInFragment())
            }
            signUp.setOnClickListener {
                findNavController().navigate(OnBoardingFragmentDirections.actionOnBoardingFragmentToSingUpFragment())
            }
        }
    }
}