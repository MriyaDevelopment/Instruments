package com.decorator1889.instruments.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.decorator1889.instruments.util.ProgressBarAnimation
import com.decorator1889.instruments.R
import com.decorator1889.instruments.databinding.DialogResultBinding
import com.decorator1889.instruments.fragments.TestFragmentDirections
import com.decorator1889.instruments.util.*
import com.decorator1889.instruments.viewModels.MainViewModel
import com.decorator1889.instruments.viewModels.ResultViewModel
import com.google.android.material.chip.Chip

class ResultDialog: DialogFragment() {

    private lateinit var binding: DialogResultBinding
    private val resultViewModel: ResultViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = DialogResultBinding.inflate(inflater, container, false).apply {
        binding = this
        bindDataResult()
        setListener()
        Log.d(Constants.RESULT_TAG, "ResultDialog created")
    }.root

    override fun onStart() {
        super.onStart()
        resultViewModel.returnOnce = false
    }

    private fun setListener() {
        binding.run {
            repeatTest.setOnClickListener {
                resultViewModel.run {
                    returnOnce = true
                    if (repeatTest.value == false) {
                        level.value?.let { level ->
                            typesCategories.value?.let { types ->
                                findNavController().navigate(TestFragmentDirections.actionTestFragmentToTestFragment(level = level, typesCategories = types))
                            }
                        }
                    } else {
                        findNavController().navigate(TestFragmentDirections.actionTestFragmentToTestFragment(repeat = true))
                    }
                }
                setResultData()
            }
            ok.setOnClickListener {
                resultViewModel.returnOnce = false
                dismiss()
            }
        }
    }

    private fun setResultData() {
        resultViewModel.run {
            level.value?.let { level ->
                allQuestion.value?.let { allQuestion->
                    correctAnswer.value?.let { correctAnswer ->
                        typesCategories.value?.let { typesCategories ->
                            questions.value?.let { questions ->
                                mainViewModel.setResultData(
                                    level = level,
                                    number_of_questions = allQuestion.toLong(),
                                    number_of_correct_answers = correctAnswer.toLong(),
                                    categories = typesCategories,
                                    questions = questions
                                )
                            }
                        }
                    }
                }
            }
        }
    }


    private fun bindDataResult() {
        resultViewModel.run {
            level.value?.let { number ->
                binding.level.text = str(R.string.testLevel, getTitleToolbar(number))
            }
            timer.value?.let { time ->
                binding.timer.text = time
            }
            allQuestion.value?.let { all ->
                resultViewModel.correctAnswer.value?.let { correct ->
                    onCreateProgress(all, correct)

                }
            }
            typesCategories.value?.let { types ->
                val list: List<String> = ArrayList(types.split(","))
                onCreateChips(list)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun onCreateProgress(all: Int, correct: Int) {
        binding.run {
            answers.text = "${correct}/${all}"
            val percent = (correct * 100)/all
            percents.text = "$percent%"
            val anim = ProgressBarAnimation(progress, 0f, percent.toFloat())
            anim.duration = 1500
            progress.startAnimation(anim)
        }
    }

    private fun onCreateChips(list: List<String>) {
        binding.run {
            chips.removeAllViews()
            list.let {
                for (index in it.indices) {
                    val chip = Chip(chips.context)
                    chip.chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(root.context, getColor25MiniCategories(it[index])))
                    chip.setTextColor(ContextCompat.getColor(root.context, getColorMiniCategories(it[index])))
                    chip.chipCornerRadius = resources.getDimension(R.dimen.corner3)
                    chip.text= getNameMiniCategories(it[index])
                    chip.isClickable = false
                    chip.isCheckable = true
                    chips.addView(chip)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppDialogTheme)
    }

    @SuppressLint("DialogFragmentCallbacksDetector")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val root = ConstraintLayout(requireContext())
        root.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        val dialog = Dialog(requireContext())
        dialog.window?.attributes?.windowAnimations = R.style.AnimationDialog
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(root)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        return dialog
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (!resultViewModel.returnOnce) {
            setResultData()
            findNavController().navigate(TestFragmentDirections.actionTestFragmentToProfileFragment())
        }
    }
}