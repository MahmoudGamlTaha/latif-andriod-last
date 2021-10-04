package com.service.khdmaa.ui.main.services

import com.service.khdmaa.data.local.AppPrefsStorage
import com.service.khdmaa.network.repo.DataRepo
import com.service.khdmaa.ui.base.CategoriesViewModel
import javax.inject.Inject

class ServiceViewModel @Inject constructor(repo: DataRepo, appPrefsStorage: AppPrefsStorage) :
    CategoriesViewModel(appPrefsStorage,repo) {




}