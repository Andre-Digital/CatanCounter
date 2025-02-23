package com.andredigital.catancounter

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences

@SuppressLint("StaticFieldLeak")
object AppObjects {
    var app: Application? = null
    var data: String? = null

    private val context: Context?
        get() = app?.applicationContext

    fun sharedPreferences(): SharedPreferences? {
        return context?.getSharedPreferences("prefs", Context.MODE_PRIVATE)
    }
}

const val KEY_ID = "key_id"
const val KEY_DATA = "key_data"

actual fun retrieveId(): String? {
    return AppObjects.sharedPreferences()?.getString(KEY_ID, null)
}

actual fun storeId(id: String) {
    AppObjects.sharedPreferences()?.edit()?.putString(KEY_ID, id)?.apply()
}

actual fun storeStateData(state: String) {
    AppObjects.sharedPreferences()?.edit()?.putString(KEY_DATA, state)?.apply()
}

actual fun retrieveStateData(): String? {
    return AppObjects.sharedPreferences()?.getString(KEY_DATA, null)
}