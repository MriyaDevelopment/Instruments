package com.decorator1889.instruments.fragments

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.decorator1889.instruments.ProgressBarAnimation
import com.decorator1889.instruments.R
import com.decorator1889.instruments.adapters.TestAdapter
import com.decorator1889.instruments.databinding.FragmentTestBinding
import com.decorator1889.instruments.dialogs.ResultDialog
import com.decorator1889.instruments.util.*
import com.decorator1889.instruments.util.enums.State
import com.decorator1889.instruments.viewModels.ResultViewModel
import com.decorator1889.instruments.viewModels.TestViewModel


class TestFragment : Fragment() {

    private lateinit var binding: FragmentTestBinding
    private val testViewModel: TestViewModel by viewModels()
    private val resultViewModel: ResultViewModel by activityViewModels()
    private val args: TestFragmentArgs by navArgs()
    private lateinit var onQuestionEvent: DefaultNetworkEventObserver

    private val testAdapter by lazy {
        TestAdapter(onClickAnswer = onClickAnswer)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentTestBinding.inflate(inflater, container, false).apply {
        binding = this
        initializeObservers()
        setObservers()
        setAdapter()
        setTitleToolbar()
        setListeners()
    }.root

    private fun initializeObservers() {
        onQuestionEvent = DefaultNetworkEventObserver(
            anchorView = binding.root,
            doOnLoading = {
                hideQuestion()
            },
            doOnSuccess = {
                loadAdapter()
                bindData()
                showQuestion()
                testViewModel.startTimer()
            }
        )
    }

    @SuppressLint("SetTextI18n")
    private fun bindData() {
        binding.run {
            testViewModel.let {
                mission.text = "Задание: ${it.currentQuestion}/${it.allQuestion}"
            }
        }
    }

    private fun hideQuestion() {
        binding.run {
            select.gone()
            progressContainer.visible()
            containerTimerProgress.alpha = 0f
            containerTimerProgress.gone()
            viewPager.alpha = 0f
            viewPager.gone()
            select.alpha = 0f
            select.gone()
            progressContainer.alpha = 1f
            progress.visible()
        }
    }

    private fun showQuestion() {
        binding.run {
            select.visible()
            select.animate().alpha(1f)
            containerTimerProgress.visible()
            containerTimerProgress.animate().alpha(1f)
            progressContainer.animate().alpha(0f)
            viewPager.visible()
            viewPager.animate().alpha(1f)
        }
    }

    private fun setObservers() {
        testViewModel.run {
            questionResultEvent.observe(viewLifecycleOwner, onQuestionEvent)
            timerTask.observe(viewLifecycleOwner) {
                binding.timer.text = it
            }
        }
    }

    private fun setAdapter() {
        binding.run {
            viewPager.isUserInputEnabled = false
            viewPager.offscreenPageLimit = 100
            viewPager.adapter = testAdapter
        }
    }

    private fun setListeners() {
        binding.run {
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            select.setOnClickListener {
                val currentItem = viewPager.currentItem
                viewPager.setCurrentItem(currentItem + 1, true)
                testViewModel.run {
                    questionList.value?.let { question ->
                        if (question.size > currentQuestion) {
                            val from = (currentQuestion * 100)/question.size
                            currentQuestion++
                            val to = (currentQuestion * 100)/question.size
                            val anim = ProgressBarAnimation(progress, from.toFloat(), to.toFloat())
                            anim.duration = 100
                            progress.startAnimation(anim)
                        } else {
                            currentQuestion = question.size
                            onTimerCancel()
                            resultViewModel.setResultDataQuest(args.typesCategories, args.level, testViewModel.allQuestion, testViewModel.correctAnswer, binding.timer.text.toString())
                            ResultDialog().show(childFragmentManager, "ResultDialog")
                        }
                    }
                }
                bindData()
            }
        }
    }

    private fun setTitleToolbar() {
        binding.toolbar.title = str(R.string.testLevel, getTitleToolbar(args.level))
    }

    private val onClickAnswer:(Long, Boolean) -> Unit = { itemId, check ->
        if (check) {
            testViewModel.setCorrectAnswers()
        }
    }

    override fun onStart() {
        super.onStart()
        checkIsLoadData()
        testViewModel.onStartTimer()
        getStatusBarColor(STATUS_BAR_BLUE)
    }

    private fun checkIsLoadData() {
        testViewModel.run {
            if (questionResultEvent.value?.peekContent() != State.SUCCESS) getQuestion(
                type = args.typesCategories,
                level = args.level
            )
            else loadAdapter()
        }
    }

    private fun loadAdapter() {
        testViewModel.questionList.value?.let { question ->
            testViewModel.allQuestion = question.size
            testAdapter.submitList(question)
        }
    }

    override fun onStop() {
        super.onStop()
        getStatusBarColor(STATUS_BAR_WHITE)
        testViewModel.onStopTimer()
    }

    private fun getStatusBarColor(type: Int) {
        val decorView: View = requireActivity().window.decorView
        if (type == 1) {
            requireActivity().window.statusBarColor =
                ContextCompat.getColor(requireContext(), R.color.blue_5B67CA)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requireActivity().window.decorView.systemUiVisibility =
                    decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
        } else if (type == 2) {
            requireActivity().window.statusBarColor =
                ContextCompat.getColor(requireContext(), R.color.white_FFFFFFFF)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requireActivity().window.decorView.systemUiVisibility =
                    decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }

    companion object {
        const val STATUS_BAR_BLUE = 1
        const val STATUS_BAR_WHITE = 2
    }
}