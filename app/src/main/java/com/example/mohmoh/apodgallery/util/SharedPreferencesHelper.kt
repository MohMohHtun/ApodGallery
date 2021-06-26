package com.example.mohmoh.apodgallery.util

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import androidx.core.content.edit

class SharedPreferencesHelper {



    companion object{
        private var prefs: SharedPreferences? = null
        private const val PREF_TIME = "Pref time";

        @Volatile private var instance: SharedPreferencesHelper? = null
        private val LOCK = Any()

        operator fun invoke(context: Context): SharedPreferencesHelper = instance ?: synchronized(LOCK) {
            instance ?: buildHeler(context).also{
                instance = it
            }
        }

        private fun buildHeler(context: Context) : SharedPreferencesHelper{
            prefs = PreferenceManager.getDefaultSharedPreferences((context))

            return  SharedPreferencesHelper()
        }
    }

    fun saveUpdateTime(time: Long){
        prefs?.edit(commit = true) {
            putLong(PREF_TIME, time)
        }
    }

    fun getUpdateTime() = prefs?.getLong(PREF_TIME, 0)

}