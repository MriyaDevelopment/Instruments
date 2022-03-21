package com.decorator1889.instruments.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decorator1889.instruments.App
import com.decorator1889.instruments.Network.ApiNetwork
import com.decorator1889.instruments.models.Question
import com.decorator1889.instruments.models.toQuestion
import com.decorator1889.instruments.util.NetworkEvent
import com.decorator1889.instruments.util.enums.Load
import com.decorator1889.instruments.util.enums.State
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class TestViewModel : ViewModel() {

    val timerTask = MutableLiveData<String>()

    private val _questionList = MutableLiveData<List<Question>>()
    val questionList: LiveData<List<Question>> = _questionList
    private val _questionResultEvent = MutableLiveData<NetworkEvent<State>>()
    val questionResultEvent: LiveData<NetworkEvent<State>> = _questionResultEvent
    var allQuestion = 0
    var currentQuestion = 1

    fun getQuestion(type: String, level: Long) {
        viewModelScope.launch {
            _questionResultEvent.value = NetworkEvent(State.LOADING)
            try {
                val response =
                    ApiNetwork.API.getQuestionByTypeAndLevelAsync(type = type, level = level)
                        .await()
                if (response.result == Load.SUCCESS.state) {
                    _questionList.value = response.questions?.toQuestion()
                    _questionResultEvent.value = NetworkEvent(State.SUCCESS)
                } else {
                    _questionList.value = listOf()
                    _questionResultEvent.value = NetworkEvent(State.ERROR, response.error)
                }
            } catch (e: Exception) {
                _questionList.value = listOf()
                _questionResultEvent.value = NetworkEvent(State.FAILURE, e.message)
            }
        }
    }

    fun getQuestionRepeat() {
        viewModelScope.launch {
            _questionResultEvent.value = NetworkEvent(State.LOADING)
            try {
                val response =
                    ApiNetwork.API.getLastTestAsync(user_token = App.getInstance().userToken)
                        .await()
                if (response.result == Load.SUCCESS.state) {
                    _questionList.value = response.questions?.toQuestion()
                    _questionResultEvent.value = NetworkEvent(State.SUCCESS)
                } else {
                    _questionList.value = listOf()
                    _questionResultEvent.value = NetworkEvent(State.ERROR, response.error)
                }
            } catch (e: Exception) {
                _questionList.value = listOf()
                _questionResultEvent.value = NetworkEvent(State.FAILURE, e.message)
            }
        }
    }

    val timer = Timer()
    var minute = 0
    var second = 0
    var timerStop = false
    var minutePost = ""

    fun startTimer() {
        timer.schedule(object : TimerTask() {
            override fun run() {
                if (timerStop) {
                    second++
                    if (second == 60) {
                        second = 0
                        minute++
                    }
                    minutePost = if (minute.toString().length < 2) {
                        "0$minute"
                    } else {
                        "$minute"
                    }
                    if (second.toString().length < 2) {
                        timerTask.postValue("$minutePost:0$second")
                    } else {
                        timerTask.postValue("$minutePost:$second")
                    }
                }
            }
        }, 1, 1000)
    }

    fun onStartTimer() {
        timerStop = true
    }

    fun onStopTimer() {
        timerStop = false
    }

    var correctAnswer = 0

    fun setCorrectAnswers() {
        correctAnswer++
    }

    fun onTimerCancel() {
        timer.cancel()
    }
}