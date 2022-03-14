package com.decorator1889.instruments.Network.response

data class LoginResponse(
    val result: String?,
    val error: String?,
    val user: User?
) {
    data class User(
        val id: Long?,
        val name: String?,
        val email: String?,
        val api_token: String?,
        val is_subscribed: Boolean?
    )
}