package com.decorator1889.instruments

import android.app.Application
import androidx.preference.PreferenceManager
import com.decorator1889.instruments.Network.ApiNetwork

class App: Application()  {

    var userToken: String? = ""


    override fun onCreate() {
        super.onCreate()
        reference = this
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        userToken = prefs.getString(PREF_USER_TOKEN, "")
    }

    fun logIn(userToken: String) {
        this.userToken = userToken
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        prefs.edit()
            .putString(PREF_USER_TOKEN, userToken)
            .apply()
    }

    fun logOut() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        prefs.edit()
            .remove(PREF_USER_TOKEN)
            .apply()
        userToken = null
    }

    companion object {
        const val DEBUG_TAG = "InstrumentsDebug"
        val LoggingEnabled = BuildConfig.DEBUG

        const val PREF_USER_TOKEN = "pref_user_token"

        private var reference: App? = null

        fun getInstance(): App {
            return reference ?: throw IllegalStateException("App is not initialized.")
        }
    }
}