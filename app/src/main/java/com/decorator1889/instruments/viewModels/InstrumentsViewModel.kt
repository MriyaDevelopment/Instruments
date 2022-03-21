package com.decorator1889.instruments.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decorator1889.instruments.App
import com.decorator1889.instruments.Network.ApiNetwork
import com.decorator1889.instruments.Network.response.RemoveLikeResponse
import com.decorator1889.instruments.models.*
import com.decorator1889.instruments.util.NetworkEvent
import com.decorator1889.instruments.util.OneTimeEvent
import com.decorator1889.instruments.util.enums.Load
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
                val response = ApiNetwork.API.getInstrumentsByTypeAsync(
                    type = type,
                    user_token = App.getInstance().userToken
                ).await()
                if (response.result == Load.SUCCESS.state) {
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
                val response = ApiNetwork.API.getSurgeryInstrumentsByTypeAsync(
                    type = type,
                    user_token = App.getInstance().userToken
                ).await()
                if (response.result == Load.SUCCESS.state) {
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

    private val _like = MutableLiveData<Like>()
    val like: LiveData<Like> = _like
    private val _likeResultEvent = MutableLiveData<NetworkEvent<State>>()
    val likeResultEvent: LiveData<NetworkEvent<State>> = _likeResultEvent
    private val _removeLikeResponse = MutableLiveData<RemoveLikeResponse>()
    val errorLikeEvent = MutableLiveData<OneTimeEvent>()

    fun setLike(instrument_id: Long, is_surgery: Boolean) {
        viewModelScope.launch {
            try {
                val response = ApiNetwork.API.setLikeAsync(
                    user_token = App.getInstance().userToken,
                    instrument_id = instrument_id,
                    is_surgery = is_surgery
                ).await()
                if (response.result == Load.SUCCESS.state) {
                    _like.value = response.like?.toLike()
                    _likeResultEvent.value = NetworkEvent(State.SUCCESS)
                } else {
                    _likeResultEvent.value = NetworkEvent(State.ERROR, response.error)
                    errorLikeEvent.value = OneTimeEvent()
                }
            } catch (e: Exception) {
                _likeResultEvent.value = NetworkEvent(State.FAILURE, e.message)
                errorLikeEvent.value = OneTimeEvent()
            }
        }
    }

    fun removeLike(instrument_id: Long, is_surgery: Boolean) {
        viewModelScope.launch {
            try {
                val response = ApiNetwork.API.removeLikeAsync(
                    user_token = App.getInstance().userToken,
                    instrument_id = instrument_id,
                    is_surgery = is_surgery
                ).await()
                if (response.result == Load.SUCCESS.state) {
                    _removeLikeResponse.value = response
                    _likeResultEvent.value = NetworkEvent(State.SUCCESS)
                } else {
                    _likeResultEvent.value = NetworkEvent(State.ERROR, response.error)
                    errorLikeEvent.value = OneTimeEvent()
                }
            } catch (e: Exception) {
                _likeResultEvent.value = NetworkEvent(State.FAILURE, e.message)
                errorLikeEvent.value = OneTimeEvent()
            }
        }
    }

    fun setNewInstrumentsList(newInstrumentsList: List<Instruments>) {
        _instruments.value = newInstrumentsList
    }
}