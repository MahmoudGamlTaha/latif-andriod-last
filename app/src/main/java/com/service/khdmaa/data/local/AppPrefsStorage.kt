package com.service.khdmaa.data.local

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.emptyPreferences
import com.service.khdmaa.data.models.UserModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppPrefsStorage  @Inject constructor(
    @ApplicationContext context: Context
)  {

    // since @Singleton scope is used, dataStore will have the same instance every timeco
    companion object {
        var token:String = ""
        var language_:String = "ar"
    }

    private val dataStore: DataStore<Preferences> =
        context.createDataStore(name = "AppPrefStorage")

    public suspend fun <T> setValue(
        key: Preferences.Key<T>,
        value: T
    ) {

        dataStore.edit { preferences ->
            // save the value in prefs
            preferences[key] = value
        }
    }

    /***
     * handy function to return Preference value based on the Preference key
     * @param key  used to identify the preference
     * @param defaultValue value in case the Preference does not exists
     * @throws Exception if there is some error in getting the value
     * @return [Flow] of [T]
     */
    public fun <T> getValueAsFlow(
        key: Preferences.Key<T>,
        defaultValue: T
    ): Flow<T> {
        return dataStore.data.catch { exception ->
                emit(emptyPreferences())
        }.map { preferences ->
            // return the default value if it doesn't exist in the storage
            preferences[key] ?: defaultValue
        }
    }


}