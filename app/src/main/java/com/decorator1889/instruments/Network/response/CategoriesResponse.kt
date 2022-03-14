package com.decorator1889.instruments.Network.response

data class CategoriesResponse(
    val result: String?,
    val error: String?,
    val category: List<Categories>?
) {
    data class Categories(
        val id: Long?,
        val name: String?,
        val type: String?,
        val number_of_questions: Long?,
        val lock: Boolean?
    )
}