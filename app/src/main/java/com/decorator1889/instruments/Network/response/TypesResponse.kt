package com.decorator1889.instruments.Network.response

data class TypesResponse(
    val result: String?,
    val error: String?,
    val types: List<Types>?
) {
    data class Types(
        val id: Long?,
        val name: String?
    )
}