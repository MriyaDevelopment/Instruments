package com.decorator1889.instruments.models

import com.decorator1889.instruments.Network.response.QuestionResponse

data class Question(
    val id: Long,
    val question: String,
    val type: String,
    val level: Long,
    val answer_one: String,
    val answer_two: String,
    val answer_three: String,
    val answer_four: String,
    val true_answer: String,
    var state: Int
)

fun List<QuestionResponse.Question>.toQuestion(): List<Question> {
    return this.map { question ->
        Question(
            id = question.id ?: 0L,
            question = question.question ?: "",
            type = question.type ?: "",
            level = question.level ?: 0L,
            answer_one = question.answer_one ?: "",
            answer_two = question.answer_two ?: "",
            answer_three = question.answer_three ?: "",
            answer_four = question.answer_four ?: "",
            true_answer = question.true_answer ?: "",
            state = 0
        )
    }
}