package com.decorator1889.instruments.models

import com.decorator1889.instruments.Network.response.ResultResponse
import com.decorator1889.instruments.Network.response.SentResultResponse

data class Result(
    val level: Long,
    val categories: String,
    val number_of_correct_answers: Long,
    val number_of_questions: Long,
    val questions: String
)

fun List<ResultResponse.Result>.toResult(): List<Result> {
    return this.map { result ->
        Result(
            level = result.level ?: 0L,
            categories = result.categories ?: "",
            number_of_correct_answers = result.number_of_correct_answers ?: 0L,
            number_of_questions = result.number_of_questions ?: 0L,
            questions = result.questions ?: ""
        )
    }
}

fun List<SentResultResponse.Result>.toSentResult(): List<Result> {
    return this.map { sentResult ->
        Result(
            level = sentResult.level ?: 0L,
            categories = sentResult.categories ?: "",
            number_of_correct_answers = sentResult.number_of_correct_answers ?: 0L,
            number_of_questions = sentResult.number_of_questions ?: 0L,
            questions = sentResult.questions ?: ""
        )
    }
}