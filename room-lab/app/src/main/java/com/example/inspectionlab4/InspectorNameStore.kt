package com.example.inspectionlab4

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.inspectionSettings by preferencesDataStore(name = "inspection_settings")
private val INSPECTOR_NAME_KEY = stringPreferencesKey("inspector_name")

object InspectorNameStore {
    fun observe(context: Context): Flow<String> =
        context.inspectionSettings.data.map { preferences ->
            preferences[INSPECTOR_NAME_KEY] ?: "未设置"
        }

    suspend fun save(context: Context, name: String) {
        context.inspectionSettings.edit { preferences ->
            preferences[INSPECTOR_NAME_KEY] = name.trim()
        }
    }
}
