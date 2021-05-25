package com.latifapp.latif.data.local

import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.preferencesKey

class PreferenceConstants {
    companion object{
        public val USER_ID_PREFS = preferencesKey<String>("userID")
        public val Lang_PREFS = preferencesKey<String>("LangID")
    }
}