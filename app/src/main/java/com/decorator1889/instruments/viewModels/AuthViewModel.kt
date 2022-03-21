package com.decorator1889.instruments.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.decorator1889.instruments.App
import com.decorator1889.instruments.Network.ApiNetwork
import com.decorator1889.instruments.models.User
import com.decorator1889.instruments.models.toLogin
import com.decorator1889.instruments.models.toRegister
import com.decorator1889.instruments.util.NetworkEvent
import com.decorator1889.instruments.util.OneTimeEvent
import com.decorator1889.instruments.util.enums.Load
import com.decorator1889.instruments.util.enums.State
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val _registerUser = MutableLiveData<User>()
    val registerUser: LiveData<User> = _registerUser
    private val _registerResultEvent = MutableLiveData<NetworkEvent<State>>()
    val registerResultEvent: LiveData<NetworkEvent<State>> = _registerResultEvent
    val errorResultEvent = MutableLiveData<OneTimeEvent>()

    fun registerUser(name: String, email: String, password: String) {
        viewModelScope.launch {
            _registerResultEvent.value = NetworkEvent(State.LOADING)
            try {
                val response = ApiNetwork.API.registerAsync(
                    name = name,
                    email = email,
                    password = password,
                    password_confirmation = password
                ).await()
                if (response.result == Load.SUCCESS.state) {
                    _registerUser.value = response.register?.toRegister()
                    registerUser.value?.api_token?.let { App.getInstance().logIn(it) }
                    _registerResultEvent.value = NetworkEvent(State.SUCCESS)
                } else {
                    _registerResultEvent.value = NetworkEvent(State.ERROR, response.error)
                    errorResultEvent.value = OneTimeEvent()
                }
            } catch (e: Exception) {
                _registerResultEvent.value = NetworkEvent(State.FAILURE, e.message)
                errorResultEvent.value = OneTimeEvent()
            }
        }
    }

    private val _loginUser = MutableLiveData<User>()
    val loginUser: LiveData<User> = _loginUser
    private val _loginResultEvent = MutableLiveData<NetworkEvent<State>>()
    val loginResultEvent: LiveData<NetworkEvent<State>> = _loginResultEvent

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            _loginResultEvent.value = NetworkEvent(State.LOADING)
            try {
                val response = ApiNetwork.API.loginAsync(email = email, password = password).await()
                if (response.result == Load.SUCCESS.state) {
                    _loginUser.value = response.user?.toLogin()
                    loginUser.value?.api_token?.let { App.getInstance().logIn(it) }
                    _loginResultEvent.value = NetworkEvent(State.SUCCESS)
                } else {
                    _loginResultEvent.value = NetworkEvent(State.ERROR, response.error)
                    errorResultEvent.value = OneTimeEvent()
                }
            } catch (e: Exception) {
                _loginResultEvent.value = NetworkEvent(State.ERROR, e.message)
                errorResultEvent.value = OneTimeEvent()
            }
        }
    }
}