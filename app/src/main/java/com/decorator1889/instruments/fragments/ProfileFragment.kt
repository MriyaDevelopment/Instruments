package com.decorator1889.instruments.fragments

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.decorator1889.instruments.MainActivity
import com.decorator1889.instruments.util.ProgressBarAnimation
import com.decorator1889.instruments.R
import com.decorator1889.instruments.databinding.FragmentProfileBinding
import com.decorator1889.instruments.dialogs.ExitDialog
import com.decorator1889.instruments.models.Result
import com.decorator1889.instruments.util.*
import com.decorator1889.instruments.util.enums.State
import com.decorator1889.instruments.viewModels.MainViewModel
import com.google.android.material.chip.Chip

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var onProfileEvent: DefaultNetworkEventObserver
    private lateinit var onMainPageEvent: DefaultNetworkEventObserver
    private lateinit var onResultEvent: DefaultNetworkEventObserver
    private lateinit var onSentResultEvent: DefaultNetworkEventObserver

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentProfileBinding.inflate(inflater, container, false).apply {
        binding = this
        initializeObservers()
        setObservers()
        setListeners()
        Log.d(Constants.PROFILE_TAG, "ProfileFragment created")
    }.root

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
            headerBlock.setOnClickListener {
                (activity as MainActivity).selectLevels()
            }
            again.setOnClickListener {
                mainViewModel.result.value?.let { resultProfile ->
                    val result = resultProfile[0]
                    findNavController().navigate(
                        ProfileFragmentDirections.actionProfileFragmentToTestFragment(
                            repeat = true,
                            typesCategories = result.categories,
                            level = result.level
                        )
                    )
                }
            }
        }
    }

    private fun setObservers() {
        mainViewModel.run {
            profileResultEvent.observe(viewLifecycleOwner, onProfileEvent)
            resultResultEvent.observe(viewLifecycleOwner, onResultEvent)
            mainPageLoading.observe(viewLifecycleOwner, onMainPageEvent)
            sentResultResultEvent.observe(viewLifecycleOwner, onSentResultEvent)
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
        onSentResultEvent = DefaultNetworkEventObserver(
            anchorView = binding.root,
            doOnSuccess = {
                mainViewModel.loadMainData()
            },
            doOnError = {
                mainViewModel.loadMainData()
            },
            doOnFailure = {
                mainViewModel.loadMainData()
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


    private fun bindResultData() {
        binding.run {
            mainViewModel.result.value?.let { resultProfile ->
                if (resultProfile.isEmpty()) {
                    resultBlock.gone()
                    headerBlock.visible()
                    return
                }
                val result = resultProfile[0]
                resultBlock.visible()
                setLevel(result)
                setProgress(result)
                loadAdapter(result)
            }
        }
    }

    private fun loadAdapter(result: Result) {
        binding.run {
            val list: List<String> = ArrayList(result.categories.split(","))
            onCreateChips(list)
        }
    }

    private fun onCreateChips(list: List<String>) {
        binding.run {
            chips.removeAllViews()
            list.let {
                for (index in it.indices) {
                    val chip = Chip(chips.context)
                    chip.chipBackgroundColor = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            root.context,
                            getColor25MiniCategories(it[index])
                        )
                    )
                    chip.setTextColor(
                        ContextCompat.getColor(
                            root.context,
                            getColorMiniCategories(it[index])
                        )
                    )
                    chip.chipCornerRadius = resources.getDimension(R.dimen.corner3)
                    chip.text = getNameMiniCategories(it[index])
                    chip.typeface = Typeface.create(
                        ResourcesCompat.getFont(
                            root.context,
                            R.font.montserrat_medium
                        ), Typeface.NORMAL
                    )
                    chip.isClickable = false
                    chip.isCheckable = true
                    chips.addView(chip)
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setProgress(result: Result) {
        binding.run {
            answers.text = "${result.number_of_correct_answers}/${result.number_of_questions}"
            val percent =
                (result.number_of_correct_answers.toInt() * 100) / result.number_of_questions.toInt()
            percents.text = "$percent%"
            val anim = ProgressBarAnimation(progress, 0f, percent.toFloat())
            anim.duration = 1000
            progress.startAnimation(anim)
        }
    }

    private fun hideProfile() {
        binding.run {
            swipeRefresh.isRefreshing = true
            containerProfile.alpha = 0f
            headerBlock.gone()
            containerProfile.gone()
        }
    }

    private fun showProfile() {
        binding.run {
            swipeRefresh.isRefreshing = false
            containerProfile.visible()
            containerProfile.animate().alpha(1f)
        }
    }

    private fun setLevel(result: Result) {
        binding.run {
            level.text = getTitleToolbar(result.level)
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