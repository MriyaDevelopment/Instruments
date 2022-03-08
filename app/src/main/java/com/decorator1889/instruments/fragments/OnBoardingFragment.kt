package com.decorator1889.instruments.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    private fun setListeners() {
        binding.run {
            signIn.setOnClickListener {
                //Todo экран авторизации
            }
            signUp.setOnClickListener {
                //Todo экран регистрации
            }
        }
    }
}