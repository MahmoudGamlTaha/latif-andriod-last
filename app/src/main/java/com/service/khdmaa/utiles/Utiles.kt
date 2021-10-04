package com.service.khdmaa.utiles

import android.app.Activity
import android.content.res.Configuration
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import com.service.khdmaa.data.local.AppPrefsStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*


object Utiles {

    var LANGUAGE:String="ar"
    fun log_D(key: Any, value: Any?) {
        Log.d("$key", "$value")
    }

    fun setMyLocationPositionInBottom(mMapView: View?) {
        val view1 = mMapView?.findViewById("1".toInt()) as View
        val locationButton: View =
            (view1
                .getParent() as View).findViewById("2".toInt())
        val rlp = locationButton.getLayoutParams() as RelativeLayout.LayoutParams
// position on right bottom
// position on right bottom
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0)
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
        rlp.setMargins(0, 0, 20, 20)
    }


    private var debounceJob: Job? = null
    fun onSearchDebounce(
        waitMs: Long = 500L,
        coroutineScope: CoroutineScope,
        destinationFunction: () -> Unit
    ) {
        debounceJob?.cancel()
        debounceJob = coroutineScope.launch {
            delay(waitMs)
            destinationFunction()
        }
    }


    fun setLocalization(activity: Activity, lang: String?) {
        val locale = Locale(lang ?: "ar")
        Locale.setDefault(locale)
        val config = Configuration()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale)
            activity.createConfigurationContext(config)
        }
        else {
            config.locale = locale
            activity.resources.updateConfiguration(config, activity.resources.getDisplayMetrics())

        }


    }
}