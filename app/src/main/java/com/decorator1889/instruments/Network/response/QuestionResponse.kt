package com.decorator1889.instruments.Network.response

data class QuestionResponse(
    val result: String?,
    val error: String?,
    val questions: List<Question>?
) {
    data class Question(
        val id: Long?,
        val question: String?,
        val type: String?,
        val level: Long?,
        val answer_one: String?,
        val answer_two: String?,
        val answer_three: String?,
        val answer_four: String?,
        val true_answer: String?
    )
}