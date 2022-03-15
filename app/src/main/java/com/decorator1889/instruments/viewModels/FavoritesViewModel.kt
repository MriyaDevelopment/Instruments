package com.decorator1889.instruments.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decorator1889.instruments.App
import com.decorator1889.instruments.Network.ApiNetwork
import com.decorator1889.instruments.models.Instruments
import com.decorator1889.instruments.models.toFavorites
import com.decorator1889.instruments.util.NetworkEvent
import com.decorator1889.instruments.util.enums.State
import kotlinx.coroutines.launch

class FavoritesViewModel : ViewModel() {

    private val _favorites = MutableLiveData<List<Instruments>>()
    val favorites: LiveData<List<Instruments>> = _favorites
    private val _favoritesResultEvent = MutableLiveData<NetworkEvent<State>>()
    val favoritesResultEvent: LiveData<NetworkEvent<State>> = _favoritesResultEvent

    fun loadFavorites() {
        viewModelScope.launch {
            _favoritesResultEvent.value = NetworkEvent(State.LOADING)
            try {
                val response = ApiNetwork.API.getFavoritesAsync(App.getInstance().userToken).await()
                if (response.result == "success") {
                    _favorites.value = response.instruments?.toFavorites()
                    _favoritesResultEvent.value = NetworkEvent(State.SUCCESS)
                } else {
                    _favorites.value = listOf()
                    _favoritesResultEvent.value = NetworkEvent(State.ERROR, response.error)
                }
            } catch (e: Exception) {
                _favorites.value = listOf()
                _favoritesResultEvent.value = NetworkEvent(State.FAILURE, e.message)
            }
        }
    }

    fun setNewFavoritesList(newFavoritesList: MutableList<Instruments>) {
        _favorites.value = newFavoritesList
    }
}