package com.latifapp.latif.ui.splash

import com.latifapp.latif.data.local.AppPrefsStorage
import com.latifapp.latif.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(appPrefsStorage: AppPrefsStorage):BaseViewModel(appPrefsStorage) {
}