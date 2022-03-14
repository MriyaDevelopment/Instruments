package com.decorator1889.instruments.models

import com.decorator1889.instruments.Network.response.CategoriesResponse
import com.decorator1889.instruments.Network.response.SubCategoriesResponse

data class Categories(
    val id: Long,
    val name: String,
    val type: String,
    val number_of_questions: Long,
    val lock: Boolean
)

fun List<CategoriesResponse.Categories>.toCategories(): List<Categories> {
    return this.map { catalog ->
        Categories(
            id = catalog.id ?: 0L,
            name = catalog.name ?: "",
            type = catalog.type ?: "",
            number_of_questions = catalog.number_of_questions ?: 0L,
            lock = catalog.lock ?: true
        )
    }
}

fun List<SubCategoriesResponse.SubCategories>.toSubCategories(): List<Categories> {
    return this.map { catalog ->
        Categories(
            id = catalog.id ?: 0L,
            name = catalog.name ?: "",
            type = catalog.type ?: "",
            number_of_questions = catalog.number_of_questions ?: 0L,
            lock = true
        )
    }
}

