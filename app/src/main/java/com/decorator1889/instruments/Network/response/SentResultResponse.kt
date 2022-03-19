package com.decorator1889.instruments.Network.response

data class SentResultResponse(
    val result: Result?
) {
    data class Result(
        val level: Long?,
        val categories: String?,
        val number_of_correct_answers: Long?,
        val number_of_questions: Long?,
        val questions: String?
    )
}