package com.decorator1889.instruments.Network.response

data class LikeResponse(
    val result: String?,
    val error: String?,
    val like: Like?
) {
    data class Like(
        val user_token: String?,
        val instrument_id: Long?
    )
}