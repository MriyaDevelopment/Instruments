package com.decorator1889.instruments.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decorator1889.instruments.App
import com.decorator1889.instruments.Network.ApiNetwork
import com.decorator1889.instruments.models.Categories
import com.decorator1889.instruments.models.User
import com.decorator1889.instruments.models.toCategories
import com.decorator1889.instruments.models.toUser
import com.decorator1889.instruments.util.NetworkEvent
import com.decorator1889.instruments.util.OneTimeEvent
import com.decorator1889.instruments.util.enums.State
import kotlinx.coroutines.launch
import java.lang.Exception

class MainViewModel : ViewModel() {

    private val _categoriesList = MutableLiveData<List<Categories>>()
    val categoriesList: LiveData<List<Categories>> = _categoriesList
    private val _categoriesResultEvent = MutableLiveData<NetworkEvent<State>>()
    val categoriesResultEvent: LiveData<NetworkEvent<State>> = _categoriesResultEvent

    private val _profile = MutableLiveData<User>()
    val profile: LiveData<User> = _profile
    private val _profileResultEvent = MutableLiveData<NetworkEvent<State>>()
    val profileResultEvent: LiveData<NetworkEvent<State>> = _profileResultEvent

    private val _mainPageLoading = MutableLiveData<NetworkEvent<State>>()
    val mainPageLoading: LiveData<NetworkEvent<State>> = _mainPageLoading
    private val mainPageLoadingMap = mutableMapOf<String, State>()

    private fun resetMainPageLoadingMap() {
        _mainPageLoading.value = NetworkEvent(State.LOADING)
        mainPageLoadingMap.run {
            clear()
        }
    }

    fun loadMainData() {
        resetMainPageLoadingMap()
        loadCategories()
        getProfileData()
    }

    private fun checkIsMainPageReady() {
        if (mainPageLoadingMap.containsValue(State.LOADING)) return
        if (mainPageLoadingMap.containsValue(State.ERROR)) {
            _mainPageLoading.postValue(NetworkEvent(State.ERROR))
            return
        }
        if (mainPageLoadingMap.containsValue(State.FAILURE)) {
            _mainPageLoading.postValue(NetworkEvent(State.FAILURE))
            return
        }
        _mainPageLoading.postValue(NetworkEvent(State.SUCCESS))
    }

    private fun loadCategories() {
        viewModelScope.launch {
            _categoriesResultEvent.value = NetworkEvent(State.LOADING)
            mainPageLoadingMap[CATEGORIES] = State.LOADING
            try {
                val response = ApiNetwork.API.getCategoriesAsync().await()
                if (response.result == "success") {
                    _categoriesList.value = response.category?.toCategories()
                    _categoriesResultEvent.value = NetworkEvent(State.SUCCESS)
                    mainPageLoadingMap[CATEGORIES] = State.SUCCESS
                } else {
                    _categoriesList.value = listOf()
                    mainPageLoadingMap[CATEGORIES] = State.ERROR
                }
            } catch (e: Exception) {
                _categoriesList.value = listOf()
                mainPageLoadingMap[CATEGORIES] = State.FAILURE
            }
            checkIsMainPageReady()
        }
    }

    private fun getProfileData() {
        viewModelScope.launch {
            _profileResultEvent.value = NetworkEvent(State.LOADING)
            mainPageLoadingMap[PROFILE] = State.LOADING
            try {
//                val response = ApiNetwork.API.getProfileDataAsync(App.getInstance().userToken).await()
//                if (response.result == "success") {
//                    _profile.value = response.user?.toUser()
                    _profileResultEvent.value = NetworkEvent(State.SUCCESS)
                    mainPageLoadingMap[PROFILE] = State.SUCCESS
//                } else {
//                    mainPageLoadingMap[PROFILE] = State.ERROR
//                }
            } catch (e: Exception) {
                mainPageLoadingMap[PROFILE] = State.FAILURE
            }
            checkIsMainPageReady()
        }
    }

    companion object {
        const val CATEGORIES = "CATEGORIES"
        const val PROFILE = "PROFILE"
    }
}