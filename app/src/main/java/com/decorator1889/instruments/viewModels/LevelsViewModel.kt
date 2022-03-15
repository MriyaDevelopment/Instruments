package com.decorator1889.instruments.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decorator1889.instruments.Network.ApiNetwork
import com.decorator1889.instruments.models.Levels
import com.decorator1889.instruments.models.toLevels
import com.decorator1889.instruments.util.NetworkEvent
import com.decorator1889.instruments.util.enums.State
import kotlinx.coroutines.launch

class LevelsViewModel: ViewModel() {

    private val _levels = MutableLiveData<List<Levels>>()
    val levels: LiveData<List<Levels>> = _levels
    private val _levelsResultEvent = MutableLiveData<NetworkEvent<State>>()
    val levelsResultEvent: LiveData<NetworkEvent<State>> = _levelsResultEvent

    fun loadLevels() {
        viewModelScope.launch {
            try {
                _levelsResultEvent.value = NetworkEvent(State.LOADING)
                val response = ApiNetwork.API.getLevelsAsync().await()
                if (response.result == "success") {
                    _levels.value = response.levels?.toLevels()
                    _levelsResultEvent.value = NetworkEvent(State.SUCCESS)
                } else {
                    _levels.value = listOf()
                    _levelsResultEvent.value = NetworkEvent(State.ERROR, response.error)
                }
            } catch (e: Exception) {
                _levels.value = listOf()
                _levelsResultEvent.value = NetworkEvent(State.FAILURE, e.message)
            }
        }
    }
}