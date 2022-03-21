package com.decorator1889.instruments.util.enums

import com.decorator1889.instruments.R
import com.decorator1889.instruments.util.str

enum class Load(val state: String) {
    SUCCESS(str(R.string.success)),
    ERROR(str(R.string.error)),
    FAILURE(str(R.string.failure))
}