package com.decorator1889.instruments.models

import com.decorator1889.instruments.Network.response.TypesResponse

data class Types(
    val id: Long,
    val name: String
)

fun List<TypesResponse.Types>.toTypes() : List<Types> {
    return this.map { types ->
        Types(
            id = types.id ?: 0L,
            name = types.name ?: ""
        )
    }
}