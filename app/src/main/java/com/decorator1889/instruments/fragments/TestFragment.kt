package com.decorator1889.instruments.fragments

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.decorator1889.instruments.MainActivity
import com.decorator1889.instruments.util.ProgressBarAnimation
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
        onFirstStartTimer()
        Log.d(Constants.TEST_TAG, "TestFragment created")
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
            },
            doOnFailure = {
                findNavController().popBackStack()
            }
        )
    }

    private fun onFirstStartTimer() {
        testViewModel.startTimer()
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
            containerTimerProgress.alpha = 0f
            viewPager.alpha = 0f
            select.alpha = 0f
            progress.alpha = 0f
            progressContainer.animate().alpha(1f)
        }
    }

    private fun showQuestion() {
        binding.run {
            select.animate().alpha(1f)
            containerTimerProgress.animate().alpha(1f)
            progress.animate().alpha(1f)
            viewPager.animate().alpha(1f)
            progressContainer.animate().alpha(0f)
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
            viewPager.adapter = testAdapter
            viewPager.offscreenPageLimit = 100
        }
    }

    private fun setListeners() {
        binding.run {
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            select.setOnClickListener {
                bindData()
                setCurrentItem()
                onSelectItem()
            }
        }
    }

    private fun onSelectItem() {
        binding.run {
            testViewModel.run {
                questionList.value?.let { question ->
                    if (question.size > currentQuestion) {
                        setProgressTest()
                        setCompleteTest()
                    } else {
                        select.isEnabled = false
                        currentQuestion = question.size
                        onTimerCancel()
                        val questionId = arrayListOf<String>()
                        for (i in question.indices) {
                            questionId.add(question[i].id.toString())
                        }
                        val questions = questionId.joinToString(",")
                        resultViewModel.setResultDataQuest(
                            args.typesCategories,
                            args.level,
                            testViewModel.allQuestion,
                            testViewModel.correctAnswer,
                            binding.timer.text.toString(),
                            questions
                        )
                        ResultDialog().show(childFragmentManager, "ResultDialog")
                    }
                }
            }
        }
    }

    private fun setCurrentItem() {
        binding.run {
            val currentItem = viewPager.currentItem
            viewPager.setCurrentItem(currentItem + 1, true)
        }
    }

    private fun setCompleteTest() {
        testViewModel.run {
            questionList.value?.let { question ->
                if (currentQuestion == question.size) {
                    binding.select.text = str(R.string.testExit)
                }
            }
        }
    }

    private fun setProgressTest() {
        testViewModel.run {
            questionList.value?.let { question ->
                val from = (currentQuestion * 100) / question.size
                currentQuestion++
                val to = (currentQuestion * 100) / question.size
                val anim = ProgressBarAnimation(binding.progress, from.toFloat(), to.toFloat())
                anim.duration = 100
                binding.progress.startAnimation(anim)
            }
        }
    }

    private fun setTitleToolbar() {
        binding.toolbar.title = str(R.string.testLevel, getTitleToolbar(args.level))
    }

    private val onClickAnswer: (Long, Boolean) -> Unit = { itemId, check ->
        if (check) {
            testViewModel.setCorrectAnswers()
        }
    }

    override fun onStart() {
        super.onStart()
        checkIsLoadData()
        onStartTimer()
        getStatusBarColor(STATUS_BAR_BLUE)
        hideBottomNavigationView()
    }

    private fun hideBottomNavigationView() {
        (activity as MainActivity).hideBottomNavigationView()
    }

    private fun onStartTimer() {
        testViewModel.onStartTimer()
    }

    private fun checkIsLoadData() {
        testViewModel.run {
            if (questionResultEvent.value?.peekContent() != State.SUCCESS) {
                if (!args.repeat) {
                    getQuestion(
                        type = args.typesCategories,
                        level = args.level
                    )
                } else {
                    getQuestionRepeat()
                }
                resultViewModel.setRepeatTest(args.repeat)
            } else loadAdapter()
        }
    }

    private fun loadAdapter() {
        testViewModel.questionList.value?.let { question ->
            if (question.isEmpty()) {
                createSnackbar(
                    binding.root,
                    getString(R.string.networkErrorMessage)
                ).show()
                findNavController().popBackStack()
                return
            }
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