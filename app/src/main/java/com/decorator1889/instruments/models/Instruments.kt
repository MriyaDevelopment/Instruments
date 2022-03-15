package com.decorator1889.instruments.models

import com.decorator1889.instruments.Network.response.FavoritesResponse
import com.decorator1889.instruments.Network.response.InstrumentsResponse

data class Instruments(
    val id: Long,
    val title: String,
    val type: String,
    val image: String,
    val full_text: String,
    var is_liked: Boolean
)

fun List<InstrumentsResponse.Instruments>.toInstruments(): List<Instruments> {
    return this.map { instruments ->
        Instruments(
            id = instruments.id ?: 0L,
            title = instruments.title ?: "",
            type = instruments.type ?: "",
            image = instruments.image ?: "",
            full_text = instruments.full_text ?: "",
            is_liked = instruments.is_liked ?: false
        )
    }
}

fun List<FavoritesResponse.Instruments>.toFavorites(): List<Instruments> {
    return this.map { favorites ->
        Instruments(
            id = favorites.id ?: 0L,
            title = favorites.title ?: "",
            type = favorites.type ?: "",
            image = favorites.image ?: "",
            full_text = favorites.full_text ?: "",
            is_liked = favorites.is_liked ?: true
        )
    }
}