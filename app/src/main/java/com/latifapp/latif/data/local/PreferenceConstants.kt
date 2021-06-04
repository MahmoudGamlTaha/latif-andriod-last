package com.latifapp.latif.data.local

import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.preferencesKey
import com.latifapp.latif.data.models.UserModel

class PreferenceConstants {
    companion object{
        public val USER_TOKEN_PREFS = preferencesKey<String>("Token")
        public val USER_UserID_PREFS = preferencesKey<String>("UserID")
        public val Lang_PREFS = preferencesKey<String>("LangID")
        public val USER_NAME = preferencesKey<String>("UserName")
        public val USER_EMAIL = preferencesKey<String>("UserEmail")
        public val USER_ADDRESS = preferencesKey<String>("UserAddress")
        public val USER_PHONE = preferencesKey<String>("UserPhone")
        public val USER_COUNTRY = preferencesKey<String>("UserCountry")
        public val USER_CITY = preferencesKey<String>("UserCity")
        public val USER_GOVS = preferencesKey<String>("UserGov")
        public val USER_REG_DATE = preferencesKey<String>("UserRegDate")
    }
}