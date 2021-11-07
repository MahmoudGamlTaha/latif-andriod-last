package com.service.khdmaa.ui

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.cloudinary.android.MediaManager
import dagger.hilt.android.HiltAndroidApp
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
@HiltAndroidApp
class MyApp :Application() {
    override fun onCreate() {
        super.onCreate()
        MediaManager.init(this)
        FacebookSdk.sdkInitialize(applicationContext)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}