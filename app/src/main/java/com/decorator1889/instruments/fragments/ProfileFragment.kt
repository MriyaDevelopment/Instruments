package com.decorator1889.instruments.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.decorator1889.instruments.MainActivity
import com.decorator1889.instruments.ProgressBarAnimation
import com.decorator1889.instruments.R
import com.decorator1889.instruments.adapters.MiniCategoriesAdapter
import com.decorator1889.instruments.databinding.FragmentProfileBinding
import com.decorator1889.instruments.dialogs.ExitDialog
import com.decorator1889.instruments.util.*
import com.decorator1889.instruments.util.enums.State
import com.decorator1889.instruments.viewModels.MainViewModel

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var onProfileEvent: DefaultNetworkEventObserver
    private lateinit var onMainPageEvent: DefaultNetworkEventObserver
    private lateinit var onResultEvent: DefaultNetworkEventObserver

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentProfileBinding.inflate(inflater, container, false).apply {
        binding = this
        initializeObservers()
        setObservers()
        itemDecorator()
        setListeners()
    }.root

    private fun itemDecorator() {
        binding.recycler.addItemDecoration(
            GridDecorations(
                sideMargins = R.dimen.margin0,
                elementsMargins = R.dimen.margin8,
                horizontalMargins = R.dimen.margin0
            )
        )
    }

    private fun setListeners() {
        binding.run {
            swipeRefresh.setOnRefreshListener {
                mainViewModel.loadMainData()
            }
            exit.setOnClickListener {
                ExitDialog().show(childFragmentManager, "ExitDialogTag")
            }
            swipeRefresh.setColorSchemeColors(
                ContextCompat.getColor(requireContext(), R.color.blue_5B67CA)
            )
        }
    }

    private fun setObservers() {
        mainViewModel.run {
            profileResultEvent.observe(viewLifecycleOwner, onProfileEvent)
            resultResultEvent.observe(viewLifecycleOwner, onResultEvent)
            mainPageLoading.observe(viewLifecycleOwner, onMainPageEvent)
        }
    }

    private fun initializeObservers() {
        onProfileEvent = DefaultNetworkEventObserver(
            anchorView = binding.root,
            doOnSuccess = {
                bindProfileData()
            }
        )
        onResultEvent = DefaultNetworkEventObserver(
            anchorView = binding.root,
            doOnSuccess = {
                bindResultData()
            }
        )
        onMainPageEvent = DefaultNetworkEventObserver(
            anchorView = binding.root,
            doOnLoading = {
                hideProfile()
            },
            doOnSuccess = {
                showProfile()
            },
        )
    }

    @SuppressLint("SetTextI18n")
    private fun bindResultData() {
        binding.run {
            mainViewModel.result.value?.let { resultProfile ->
                if (resultProfile.isEmpty()) {
                    blockTest.gone()
                    return
                }
                val result = resultProfile[0]
                when (result.level) {
                    1L -> {
                        level.text = easy
                    }
                    2L -> {
                        level.text = middle
                    }
                    else -> {
                        level.text = hard
                    }
                }
                answers.text = "${result.number_of_correct_answers}/${result.number_of_questions}"
                val percent = (result.number_of_correct_answers.toInt() * 100)/result.number_of_questions.toInt()
                percents.text = "$percent%"
                val anim = ProgressBarAnimation(progress, 0f, percent.toFloat())
                anim.duration = 1000
                progress.startAnimation(anim)
                val lst: List<String> = ArrayList(result.categories.split(","))
                val adapter = MiniCategoriesAdapter()
                recycler.adapter = adapter
                adapter.submitList(lst)
            }
        }
    }

    private fun hideProfile() {
        binding.run {
            swipeRefresh.isRefreshing = true
            containerProfile.alpha = 0f
            containerProfile.gone()
        }
    }

    private fun showProfile() {
        binding.run {
            containerProfile.visible()
            swipeRefresh.isRefreshing = false
            containerProfile.animate().alpha(1f)
        }
    }

    override fun onStart() {
        super.onStart()
        checkIsLoadData()
        (activity as MainActivity).run {
            checkBnvMenuItem(R.id.profile)
            showBottomNavigationView()
        }
    }

    private fun checkIsLoadData() {
        mainViewModel.run {
            if (profileResultEvent.value?.peekContent() != State.SUCCESS) getProfileData()
            else {
                bindResultData()
                bindProfileData()
            }
        }
    }

    private fun bindProfileData() {
        binding.run {
            mainViewModel.profile.value?.let { profile ->
                if (profile.isEmpty()) {
                    return
                }
                val user = profile[0]
                name.text = user.name
                email.text = user.email
            }
        }
    }
}