package com.decorator1889.instruments.Network.response

data class RegisterResponse(
    val result: String?,
    val error: String?,
    val register: User?
) {
    data class User(
        val name: String?,
        val email: String?,
        val api_token: String?,
        val id: Long?
    )
}