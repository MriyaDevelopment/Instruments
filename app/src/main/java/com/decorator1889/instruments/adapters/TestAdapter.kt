package com.decorator1889.instruments.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.decorator1889.instruments.App
import com.decorator1889.instruments.R
import com.decorator1889.instruments.databinding.ViewTestBinding
import com.decorator1889.instruments.models.Question
import com.decorator1889.instruments.util.glide
import com.decorator1889.instruments.util.str

class TestAdapter(
    private val onClickAnswer: (Long, Boolean) -> Unit = { itemId, check -> },
    private val onClickSelect: () -> Unit = {},
) : ListAdapter<Question, TestAdapter.TestViewHolder>(TestDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ViewTestBinding.inflate(
            layoutInflater,
            parent,
            false
        )
        return TestViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TestViewHolder, position: Int) {
        holder.bind(
            getItem(position),
            onClickAnswer,
            onClickSelect,
            position
        )
    }

    inner class TestViewHolder(
        private val binding: ViewTestBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val width2 = App.getInstance().resources.getDimensionPixelSize(R.dimen.size2dp)

        fun bind(
            item: Question,
            onClickAnswer: (Long, Boolean) -> Unit,
            onClickSelect: () -> Unit,
            position: Int,
        ) {
            binding.run {
                image.glide(item.question)
                answerOne.text = item.answer_one
                answerTwo.text = item.answer_two
                answerThree.text = item.answer_three
                answerFour.text = item.answer_four
                checkState(
                    item.state,
                    item.true_answer,
                    item.answer_one,
                    item.answer_two,
                    item.answer_three,
                    item.answer_four
                )
                if (currentList.size-1 == position) {
                    select.text = str(R.string.testExit)
                }
                answerOne.setOnClickListener {
                    select.isEnabled = true
                    if (item.true_answer == item.answer_one) {
                        item.state = 1
                        checkState(
                            item.state,
                            item.true_answer,
                            item.answer_one,
                            item.answer_two,
                            item.answer_three,
                            item.answer_four
                        )
                        onClickAnswer(item.id, true)
                    } else {
                        item.state = 1
                        checkState(
                            item.state,
                            item.true_answer,
                            item.answer_one,
                            item.answer_two,
                            item.answer_three,
                            item.answer_four
                        )
                        onClickAnswer(item.id, false)
                    }
                }
                answerTwo.setOnClickListener {
                    select.isEnabled = true
                    if (item.true_answer == item.answer_two) {
                        item.state = 2
                        checkState(
                            item.state,
                            item.true_answer,
                            item.answer_one,
                            item.answer_two,
                            item.answer_three,
                            item.answer_four
                        )
                        onClickAnswer(item.id, true)
                    } else {
                        item.state = 2
                        checkState(
                            item.state,
                            item.true_answer,
                            item.answer_one,
                            item.answer_two,
                            item.answer_three,
                            item.answer_four
                        )
                        onClickAnswer(item.id, false)
                    }
                }
                answerThree.setOnClickListener {
                    select.isEnabled = true
                    if (item.true_answer == item.answer_three) {
                        item.state = 3
                        checkState(
                            item.state,
                            item.true_answer,
                            item.answer_one,
                            item.answer_two,
                            item.answer_three,
                            item.answer_four
                        )
                        onClickAnswer(item.id, true)
                    } else {
                        item.state = 3
                        checkState(
                            item.state,
                            item.true_answer,
                            item.answer_one,
                            item.answer_two,
                            item.answer_three,
                            item.answer_four
                        )
                        onClickAnswer(item.id, false)
                    }
                }
                answerFour.setOnClickListener {
                    select.isEnabled = true
                    if (item.true_answer == item.answer_four) {
                        item.state = 4
                        checkState(
                            item.state,
                            item.true_answer,
                            item.answer_one,
                            item.answer_two,
                            item.answer_three,
                            item.answer_four
                        )
                        onClickAnswer(item.id, true)
                    } else {
                        item.state = 4
                        checkState(
                            item.state,
                            item.true_answer,
                            item.answer_one,
                            item.answer_two,
                            item.answer_three,
                            item.answer_four
                        )
                        onClickAnswer(item.id, false)
                    }
                }
                select.setOnClickListener {
                    onClickSelect()
                }
            }
        }

        private fun checkState(
            state: Int,
            trueAnswer: String,
            answerOne: String,
            answerTwo: String,
            answerThree: String,
            answerFour: String
        ) {
            when (state) {
                1 -> {
                    checkTrueAnswerOne(trueAnswer, answerOne)
                }
                2 -> {
                    checkTrueAnswerTwo(trueAnswer, answerTwo)
                }
                3 -> {
                    checkTrueAnswerThree(trueAnswer, answerThree)
                }
                4 -> {
                    checkTrueAnswerFour(trueAnswer, answerFour)
                }
            }
        }

        private fun checkTrueAnswerOne(trueAnswer: String, answerOneText: String) {
            binding.run {
                blockButton()
                answerOne.strokeWidth = width2
                if (answerOneText == trueAnswer) answerOne.setStrokeColorResource(R.color.green_81E89E)
                else answerOne.setStrokeColorResource(R.color.red_E77D7D)
            }
        }

        private fun checkTrueAnswerTwo(trueAnswer: String, answerTwoText: String) {
            binding.run {
                blockButton()
                answerTwo.strokeWidth = width2
                if (trueAnswer == answerTwoText) answerTwo.setStrokeColorResource(R.color.green_81E89E)
                else answerTwo.setStrokeColorResource(R.color.red_E77D7D)
            }
        }

        private fun checkTrueAnswerThree(trueAnswer: String, answerThreeText: String) {
            binding.run {
                blockButton()
                answerThree.strokeWidth = width2
                if (trueAnswer == answerThreeText) answerThree.setStrokeColorResource(R.color.green_81E89E)
                else answerThree.setStrokeColorResource(R.color.red_E77D7D)
            }
        }

        private fun checkTrueAnswerFour(trueAnswer: String, answerFourText: String) {
            binding.run {
                blockButton()
                answerFour.strokeWidth = width2
                if (trueAnswer == answerFourText) answerFour.setStrokeColorResource(R.color.green_81E89E)
                else answerFour.setStrokeColorResource(R.color.red_E77D7D)
            }
        }

        private fun blockButton() {
            binding.run {
                answerOne.isEnabled = false
                answerTwo.isEnabled = false
                answerThree.isEnabled = false
                answerFour.isEnabled = false
            }
        }
    }

    class TestDiffUtilCallback : DiffUtil.ItemCallback<Question>() {
        override fun areItemsTheSame(oldItem: Question, newItem: Question): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Question, newItem: Question): Boolean =
            oldItem == newItem
    }
}