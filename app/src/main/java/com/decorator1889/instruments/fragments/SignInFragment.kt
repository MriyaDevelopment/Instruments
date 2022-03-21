package com.decorator1889.instruments.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.decorator1889.instruments.MainActivity
import com.decorator1889.instruments.databinding.FragmentSignInBinding
import com.decorator1889.instruments.util.*
import com.decorator1889.instruments.viewModels.AuthViewModel

class SignInFragment : Fragment() {

    private lateinit var binding: FragmentSignInBinding
    private val authViewModel: AuthViewModel by activityViewModels()
    private lateinit var onLoginEvent: DefaultNetworkEventObserver

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentSignInBinding.inflate(inflater, container, false).apply {
        binding = this
        initializeObservers()
        setObservers()
        setListeners()
        Log.d(Constants.SIGN_IN_TAG, "SignInFragment created")
    }.root

    private fun bindData() {
        authViewModel.registerUser.value?.let { user ->
            if (user.email.isNotEmpty()) {
                binding.run {
                    signUp.gone()
                    email.setText(user.email)
                }
            }
        }
    }

    private fun setObservers() {
        authViewModel.run {
            loginResultEvent.observe(viewLifecycleOwner, onLoginEvent)
            errorResultEvent.observe(viewLifecycleOwner, OneTimeEvent.Observer {
                errorLogin()
            })
        }
    }

    private fun errorLogin() {
        binding.run {
            signIn.visible()
            loader.gone()
        }
    }

    private fun loadingLogin() {
        binding.run {
            signIn.invisible()
            loader.visible()
        }
    }

    private fun loginSuccess() {
        binding.run {
            signIn.invisible()
            loader.visible()
        }
        findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToMainFragment())
    }

    private fun initializeObservers() {
        onLoginEvent = DefaultNetworkEventObserver(
            anchorView = binding.root,
            doOnLoading = {
                loadingLogin()
            },
            doOnSuccess = {
                loginSuccess()
            },
            doOnError = {
                errorLogin()
            },
            doOnFailure = {
                errorLogin()
            }
        )
    }

    override fun onStart() {
        super.onStart()
        bindData()
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
                loginUser()
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
            password.addTextChangedListener {
                onEnabledSignInButton()
            }
            email.addTextChangedListener {
                onEnabledSignInButton()
            }
            password.setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (signIn.isEnabled) {
                        loginUser()
                    } else {
                        hideKeyboard()
                    }
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }
        }
    }

    private fun hideKeyboard() {
        hideKeyboard(binding.root.windowToken)
    }

    private fun loginUser() {
        binding.run {
            authViewModel.loginUser(
                email = email.text.toString(),
                password = password.text.toString()
            )
        }
        hideKeyboard()
    }

    private fun onEnabledSignInButton() {
        binding.run {
            signIn.isEnabled =
                checkSpacesOrNotEmpty(email.text.toString()) && checkSpacesOrNotEmpty(
                    password.text.toString()
                ) && password.length() > 5
        }
    }
}