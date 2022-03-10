package com.decorator1889.instruments.models

import com.decorator1889.instruments.util.easy
import com.decorator1889.instruments.util.hard
import com.decorator1889.instruments.util.middle

data class TestLevel(
    val id: Long,
    val name: String,
    val count: String
)

fun getTestLevel(): List<TestLevel> {
    return listOf(
        TestLevel(1, easy, "25"),
        TestLevel(2, middle, "30"),
        TestLevel(3, hard, "35")
    )
}