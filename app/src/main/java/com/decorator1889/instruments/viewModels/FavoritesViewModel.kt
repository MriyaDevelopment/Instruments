package com.decorator1889.instruments.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decorator1889.instruments.App
import com.decorator1889.instruments.Network.ApiNetwork
import com.decorator1889.instruments.Network.response.RemoveLikeResponse
import com.decorator1889.instruments.models.Instruments
import com.decorator1889.instruments.models.Like
import com.decorator1889.instruments.models.toFavorites
import com.decorator1889.instruments.util.NetworkEvent
import com.decorator1889.instruments.util.OneTimeEvent
import com.decorator1889.instruments.util.enums.Load
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
                if (response.result == Load.SUCCESS.state) {
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

    private val _like = MutableLiveData<Like>()
    val like: LiveData<Like> = _like
    private val _likeResultEvent = MutableLiveData<NetworkEvent<State>>()
    val likeResultEvent: LiveData<NetworkEvent<State>> = _likeResultEvent
    private val _removeLikeResponse = MutableLiveData<RemoveLikeResponse>()
    val errorLikeEvent = MutableLiveData<OneTimeEvent>()

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
}