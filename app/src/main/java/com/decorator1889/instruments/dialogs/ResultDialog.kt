package com.decorator1889.instruments.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.decorator1889.instruments.MainActivity
import com.decorator1889.instruments.ProgressBarAnimation
import com.decorator1889.instruments.R
import com.decorator1889.instruments.adapters.MiniCategoriesAdapter
import com.decorator1889.instruments.databinding.DialogExitBinding
import com.decorator1889.instruments.databinding.DialogResultBinding
import com.decorator1889.instruments.fragments.TestCategoriesFragmentDirections
import com.decorator1889.instruments.fragments.TestFragmentDirections
import com.decorator1889.instruments.util.GridDecorations
import com.decorator1889.instruments.util.getTitleToolbar
import com.decorator1889.instruments.util.str
import com.decorator1889.instruments.viewModels.MainViewModel
import com.decorator1889.instruments.viewModels.ResultViewModel

class ResultDialog: DialogFragment() {

    private lateinit var binding: DialogResultBinding
    private val resultViewModel: ResultViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = DialogResultBinding.inflate(inflater, container, false).apply {
        binding = this
        itemDecorator()
        bindDataResult()
        setListener()
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
            }
            ok.setOnClickListener {
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
                                findNavController().navigate(TestFragmentDirections.actionTestFragmentToProfileFragment())
                            }
                        }
                    }
                }
            }
        }
    }

    private fun itemDecorator() {
        binding.recycler.addItemDecoration(
            GridDecorations(
                sideMargins = R.dimen.margin0,
                elementsMargins = R.dimen.margin8,
                horizontalMargins = R.dimen.margin0
            )
        )
    }

    @SuppressLint("SetTextI18n")
    private fun bindDataResult() {
        binding.run {
            resultViewModel.level.value?.let { number ->
                level.text = str(R.string.testLevel, getTitleToolbar(number))
            }
            resultViewModel.timer.value?.let { time ->
                timer.text = time
            }
            resultViewModel.allQuestion.value?.let { all ->
                resultViewModel.correctAnswer.value?.let { correct ->
                    answers.text = "${correct}/${all}"
                    val percent = (correct * 100)/all
                    percents.text = "$percent%"
                    val anim = ProgressBarAnimation(progress, 0f, percent.toFloat())
                    anim.duration = 1500
                    progress.startAnimation(anim)
                }
            }
            resultViewModel.typesCategories.value?.let { types ->
                val lst: List<String> = ArrayList(types.split(","))
                val adapter = MiniCategoriesAdapter()
                recycler.adapter = adapter
                adapter.submitList(lst)
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
        }
    }
}