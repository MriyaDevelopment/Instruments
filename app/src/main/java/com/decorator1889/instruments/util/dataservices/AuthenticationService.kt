package com.decorator1889.instruments.util.dataservices

object AuthenticationService {
    private var userToken: String = ""

    private fun saveToken(token: String) {
        userToken = token
        PreferencesService.userToken.insert(token)
    }

    fun getUserToken(): String {
        return if (userToken.isNotEmpty())
            userToken
        else
            PreferencesService.userToken()
    }

    fun isAuthorize() =
        userToken.isNotEmpty() || PreferencesService.userToken().isNotEmpty()
}