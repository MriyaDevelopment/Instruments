package com.decorator1889.instruments.Network.response

import com.decorator1889.instruments.Network.response.LikeResponse

data class RemoveLikeResponse(
    val result: String?,
    val error: String?,
    val like: Long?
)