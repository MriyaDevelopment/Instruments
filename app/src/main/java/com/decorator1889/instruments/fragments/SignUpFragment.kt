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
import com.decorator1889.instruments.databinding.FragmentSingUpBinding
import com.decorator1889.instruments.util.*
import com.decorator1889.instruments.viewModels.AuthViewModel

class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSingUpBinding
    private val authViewModel: AuthViewModel by activityViewModels()

    private lateinit var onRegisterUserEvent: DefaultNetworkEventObserver

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentSingUpBinding.inflate(inflater, container, false).apply {
        binding = this
        initializeObservers()
        setObservers()
        setListeners()
        Log.d(Constants.SIGN_UP_TAG, "SignUpFragment created")
    }.root

    private fun setObservers() {
        authViewModel.run {
            registerResultEvent.observe(viewLifecycleOwner, onRegisterUserEvent)
            errorResultEvent.observe(viewLifecycleOwner, OneTimeEvent.Observer {
                errorRegister()
            })
        }
    }

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

    private fun initializeObservers() {
        onRegisterUserEvent = DefaultNetworkEventObserver(
            anchorView = binding.root,
            doOnLoading = {
                loadingRegister()
            },
            doOnSuccess = {
                loginSuccess()
            },
            doOnError = {
                errorRegister()
            },
            doOnFailure = {
                errorRegister()
            }
        )
    }

    private fun errorRegister() {
        binding.run {
            signUp.visible()
            loader.gone()
        }
    }

    private fun loginSuccess() {
        binding.run {
            signUp.visible()
            loader.gone()
        }
        findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToMainFragment())
    }

    private fun loadingRegister() {
        binding.run {
            signUp.invisible()
            loader.visible()
        }
    }

    private fun setListeners() {
        binding.run {
            signIn.setOnClickListener {
                findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToSignInFragment())
            }
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            signUp.setOnClickListener {
                registerUser()
            }
            name.addTextChangedListener {
                onEnabledSignUpButton()
            }
            email.addTextChangedListener {
                onEnabledSignUpButton()
            }
            password.addTextChangedListener {
                onEnabledSignUpButton()
                if (password.length() >= 6) hintPassword.invisible()
                else hintPassword.visible()
            }
            password.setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (signUp.isEnabled) {
                        registerUser()
                    } else {
                        hideKeyboard()
                    }
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }
        }
    }

    private fun registerUser() {
        binding.run {
            authViewModel.registerUser(
                name = name.text.toString(),
                email = email.text.toString(),
                password = password.text.toString()
            )
        }
        hideKeyboard()
    }

    private fun hideKeyboard() {
        hideKeyboard(binding.root.windowToken)
    }

    private fun onEnabledSignUpButton() {
        binding.run {
            signUp.isEnabled =
                checkSpacesOrNotEmpty(name.text.toString()) && checkSpacesOrNotEmpty(email.text.toString()) && checkSpacesOrNotEmpty(
                    password.text.toString()) && password.length() > 5
        }
    }
}