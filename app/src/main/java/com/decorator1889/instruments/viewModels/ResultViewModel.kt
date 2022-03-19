package com.decorator1889.instruments.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.decorator1889.instruments.util.OneTimeEvent

class ResultViewModel: ViewModel() {

    private val _typesCategories = MutableLiveData<String>()
    private val _level = MutableLiveData<Long>()
    private val _allQuestion = MutableLiveData<Int>()
    private val _correctAnswer = MutableLiveData<Int>()
    private val _timer = MutableLiveData<String>()
    private val _questions = MutableLiveData<String>()
    private val _repeatTest = MutableLiveData<Boolean>()

    val typesCategories: LiveData<String> = _typesCategories
    val level: LiveData<Long> = _level
    val allQuestion: LiveData<Int> = _allQuestion
    val correctAnswer: LiveData<Int> = _correctAnswer
    val timer: LiveData<String> = _timer
    val questions: LiveData<String> = _questions
    val repeatTest: LiveData<Boolean> = _repeatTest

    var returnOnce = false

    fun setResultDataQuest(
        typesCategories: String,
        level: Long,
        allQuestion: Int,
        correctAnswer: Int,
        timer: String,
        questions: String
    ) {
        _typesCategories.value = typesCategories
        _level.value = level
        _allQuestion.value = allQuestion
        _correctAnswer.value = correctAnswer
        _timer.value = timer
        _questions.value = questions
    }

    fun setRepeatTest(repeat: Boolean) {
        _repeatTest.value = repeat
    }

    val onUpdateResult = MutableLiveData<OneTimeEvent>()

    fun onUpdateProfileResult() {
        onUpdateResult.value = OneTimeEvent()
    }
}