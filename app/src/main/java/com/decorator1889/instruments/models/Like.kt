package com.decorator1889.instruments.models

import com.decorator1889.instruments.Network.response.LikeResponse

data class Like(
    val user_token: String,
    val instrument_id: Long
)

fun LikeResponse.Like.toLike(): Like {
    return Like(
        user_token = this.user_token ?: "",
        instrument_id = this.instrument_id ?: 0L
    )
}