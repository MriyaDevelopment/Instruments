package com.decorator1889.instruments.util.enums

import com.decorator1889.instruments.R
import com.decorator1889.instruments.util.str

enum class TypesTest(val level: String) {
    EASY(str(R.string.easy)),
    MIDDLE(str(R.string.middle)),
    HARD(str(R.string.hard))
}