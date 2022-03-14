package com.decorator1889.instruments.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decorator1889.instruments.Network.ApiNetwork
import com.decorator1889.instruments.models.Instruments
import com.decorator1889.instruments.models.toInstruments
import com.decorator1889.instruments.util.NetworkEvent
import com.decorator1889.instruments.util.enums.State
import kotlinx.coroutines.launch

class InstrumentsViewModel : ViewModel() {

    private val _instruments = MutableLiveData<List<Instruments>>()
    val instruments: LiveData<List<Instruments>> = _instruments
    private val _instrumentsResultEvent = MutableLiveData<NetworkEvent<State>>()
    val instrumentsResultEvent: LiveData<NetworkEvent<State>> = _instrumentsResultEvent

    fun loadInstruments(type: String) {
        viewModelScope.launch {
            _instrumentsResultEvent.value = NetworkEvent(State.LOADING)
            try {
                val response = ApiNetwork.API.getInstrumentsByTypeAsync(type).await()
                if (response.result == "success") {
                    _instruments.value = response.instruments?.toInstruments()
                    _instrumentsResultEvent.value = NetworkEvent(State.SUCCESS)
                } else {
                    _instruments.value = listOf()
                    _instrumentsResultEvent.value = NetworkEvent(State.ERROR, response.error)
                }
            } catch (e: Exception) {
                _instruments.value = listOf()
                _instrumentsResultEvent.value = NetworkEvent(State.FAILURE, e.message)
            }
        }
    }

    fun loadSurgeryInstruments(type: String) {
        viewModelScope.launch {
            _instrumentsResultEvent.value = NetworkEvent(State.LOADING)
            try {
                val response = ApiNetwork.API.getSurgeryInstrumentsByTypeAsync(type).await()
                if (response.result == "success") {
                    _instruments.value = response.instruments?.toInstruments()
                    _instrumentsResultEvent.value = NetworkEvent(State.SUCCESS)
                } else {
                    _instruments.value = listOf()
                    _instrumentsResultEvent.value = NetworkEvent(State.ERROR, response.error)
                }
            } catch (e: Exception) {
                _instruments.value = listOf()
                _instrumentsResultEvent.value = NetworkEvent(State.FAILURE, e.message)
            }
        }
    }
}