package com.decorator1889.instruments.util

import androidx.preference.PreferenceManager
import com.decorator1889.instruments.App

sealed class Preference

class StringPref private constructor(private val name: String) : Preference() {
    private val prefs by lazy {
        PreferenceManager.getDefaultSharedPreferences(App.getInstance().applicationContext)
    }
    operator fun invoke(): String {
        return prefs.getString(name, "") ?: ""
    }

    infix fun insert(value: String) {
        prefs.edit().putString(name, value).apply()
    }

    fun isExist(): Boolean {
        return prefs.contains(name)
    }

    fun remove() {
        prefs.edit().remove(name).apply()
    }

    companion object {
        infix fun new(name: String) = StringPref(name)
    }
}

class LongPref private constructor(private val name: String) : Preference() {
    private val prefs by lazy {
        PreferenceManager.getDefaultSharedPreferences(App.getInstance().applicationContext)
    }
    operator fun invoke(): Long {
        return prefs.getLong(name, 0)
    }

    infix fun insert(value: Long) {
        prefs.edit().putLong(name, value).apply()
    }

    fun isExist(): Boolean {
        return prefs.contains(name)
    }

    fun remove() {
        prefs.edit().remove(name).apply()
    }

    companion object {
        infix fun new(name: String) = LongPref(name)
    }
}

class BooleanPref private constructor(private val name: String) : Preference() {
    private val prefs by lazy {
        PreferenceManager.getDefaultSharedPreferences(App.getInstance().applicationContext)
    }
    operator fun invoke(): Boolean {
        return prefs.getBoolean(name, false)
    }

    infix fun insert(value: Boolean) {
        prefs.edit().putBoolean(name, value).apply()
    }

    fun isExist(): Boolean {
        return prefs.contains(name)
    }

    fun remove() {
        prefs.edit().remove(name).apply()
    }

    companion object {
        infix fun new(name: String) = BooleanPref(name)
    }
}