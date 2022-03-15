package com.decorator1889.instruments.Network.response

data class LevelsResponse(
    val result: String?,
    val error: String?,
    val levels: List<Levels>?
) {
    data class Levels(
        val id: Long?,
        val name: String?,
        val number_of_questions: Long?
    )
}