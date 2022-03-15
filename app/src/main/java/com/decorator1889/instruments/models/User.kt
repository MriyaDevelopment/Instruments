package com.decorator1889.instruments.models

import com.decorator1889.instruments.Network.response.LoginResponse
import com.decorator1889.instruments.Network.response.ProfileResponse
import com.decorator1889.instruments.Network.response.RegisterResponse

data class User(
    val name: String,
    val email: String,
    val api_token: String,
    val id: Long,
    val is_subscribed: Boolean?
)


fun RegisterResponse.User.toRegister() : User {
    return User(
        name = this.name ?: "",
        email = this.email ?: "",
        api_token = this.api_token ?: "",
        id = this.id ?: 0L,
        is_subscribed = false
    )
}

fun LoginResponse.User.toLogin() : User {
    return User(
        name = this.name ?: "",
        email = this.email ?: "",
        api_token = this.api_token ?: "",
        id = this.id ?: 0L,
        is_subscribed = this.is_subscribed ?: false
    )
}

fun List<ProfileResponse.User>.toListUser() : List<User> {
    return this.map { user->
        User(
            id = user.id ?: 0L,
            name = user.name ?: "",
            email = user.email ?: "",
            api_token = user.api_token ?: "",
            is_subscribed = user.is_subscribed ?: false
        )
    }
}