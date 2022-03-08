package com.decorator1889.instruments

import android.app.Application

class App: Application()  {

    override fun onCreate() {
        super.onCreate()
        reference = this
    }

    companion object {
        const val DEBUG_TAG = "InstrumentsDebug"
        val LoggingEnabled = BuildConfig.DEBUG

        private var reference: App? = null

        fun getInstance(): App {
            return reference ?: throw IllegalStateException("App is not initialized.")
        }
    }
}