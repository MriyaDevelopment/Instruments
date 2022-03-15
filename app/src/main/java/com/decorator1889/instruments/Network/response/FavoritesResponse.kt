package com.decorator1889.instruments.Network.response

data class FavoritesResponse(
    val result: String?,
    val error: String?,
    val instruments: List<Instruments>?
) {
    data class Instruments(
        val id: Long?,
        val title: String?,
        val type: String?,
        val image: String?,
        val full_text: String?,
        var is_liked: Boolean?
    )
}