package com.decorator1889.instruments.models

import com.decorator1889.instruments.Network.response.FavoritesResponse
import com.decorator1889.instruments.Network.response.LevelsResponse

data class Levels(
    val id: Long,
    val name: String,
    val number_of_questions: Long
)

fun List<LevelsResponse.Levels>.toLevels(): List<Levels> {
    return this.map { levels ->
        Levels(
            id = levels.id ?: 0L,
            name = levels.name ?: "",
            number_of_questions = levels.number_of_questions ?: 0L
        )
    }
}

