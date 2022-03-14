package com.decorator1889.instruments.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decorator1889.instruments.Network.ApiNetwork
import com.decorator1889.instruments.models.Categories
import com.decorator1889.instruments.models.toCategories
import com.decorator1889.instruments.util.NetworkEvent
import com.decorator1889.instruments.util.OneTimeEvent
import com.decorator1889.instruments.util.enums.State
import kotlinx.coroutines.launch
import java.lang.Exception

class MainViewModel: ViewModel() {

    private val _categoriesList = MutableLiveData<List<Categories>>()
    val categoriesList: LiveData<List<Categories>> = _categoriesList
    private val _categoriesResultEvent = MutableLiveData<NetworkEvent<State>>()
    val categoriesResultEvent: LiveData<NetworkEvent<State>> = _categoriesResultEvent

    fun loadCategories() {
        viewModelScope.launch {
            _categoriesResultEvent.value = NetworkEvent(State.LOADING)
            try {
                val response = ApiNetwork.API.getCategoriesAsync().await()
                if (response.result == "success") {
                    _categoriesList.value = response.category?.toCategories()
                    _categoriesResultEvent.value = NetworkEvent(State.SUCCESS)
                } else {
                    _categoriesList.value = listOf()
                    _categoriesResultEvent.value = NetworkEvent(State.ERROR, response.error)
                }
            } catch (e: Exception) {
                _categoriesList.value = listOf()
                _categoriesResultEvent.value  = NetworkEvent(State.FAILURE, e.message)
            }
        }
    }
}