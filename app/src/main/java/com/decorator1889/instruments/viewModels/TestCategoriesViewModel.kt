package com.decorator1889.instruments.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decorator1889.instruments.Network.ApiNetwork
import com.decorator1889.instruments.models.Types
import com.decorator1889.instruments.models.toTypes
import com.decorator1889.instruments.util.NetworkEvent
import com.decorator1889.instruments.util.enums.Load
import com.decorator1889.instruments.util.enums.State
import kotlinx.coroutines.launch

class TestCategoriesViewModel: ViewModel() {

    private val _types = MutableLiveData<List<Types>>()
    val types: LiveData<List<Types>> = _types
    private val _typesResultEvent = MutableLiveData<NetworkEvent<State>>()
    val typesResultEvent: LiveData<NetworkEvent<State>> = _typesResultEvent
    private val _typesForTest = MutableLiveData<MutableList<String>>()
    val typesForTest: LiveData<MutableList<String>> = _typesForTest

    fun loadTypesTestCategories() {
        viewModelScope.launch {
            _typesResultEvent.value = NetworkEvent(State.LOADING)
            try {
                val response = ApiNetwork.API.getTypesAsync().await()
                if (response.result == Load.SUCCESS.state) {
                    _types.value = response.types?.toTypes()
                    _typesResultEvent.value = NetworkEvent(State.SUCCESS)
                } else {
                    _types.value = listOf()
                    _typesResultEvent.value = NetworkEvent(State.ERROR, response.error)
                }
            } catch (e: Exception) {
                _types.value = listOf()
                _typesResultEvent.value = NetworkEvent(State.FAILURE, e.message)
            }
        }
    }

    private val stringList = mutableListOf<String>()
    var start = false
    var select = false

    fun addListCategoriesForTest(name: String) {
        stringList.add(name)
        _typesForTest.value = stringList
    }

    fun removeListCategoriesForTest(name: String) {
        stringList.remove(name)
        _typesForTest.value = stringList
    }

    fun clearListCategoriesForTest() {
        select = false
        stringList.clear()
        _typesForTest.value?.clear()
    }
}