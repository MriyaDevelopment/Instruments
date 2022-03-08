package com.decorator1889.instruments.util

class NetworkEvent<T>(
    content: T,
    val error: String? = null
) : Event<T>(content)