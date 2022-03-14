package com.decorator1889.instruments.Network.response

data class SubCategoriesResponse(
    val result: String?,
    val error: String?,
    val subcategory: List<SubCategories>?
) {
    data class SubCategories(
        val id: Long?,
        val name: String?,
        val type: String?,
        val number_of_questions: Long?
    )
}