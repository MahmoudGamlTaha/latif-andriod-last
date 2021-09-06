package com.service.khdmaa.ui.main.services

import com.service.khdmaa.data.local.AppPrefsStorage
import com.service.khdmaa.network.repo.DataRepo
import com.service.khdmaa.ui.base.BaseViewModel
import com.service.khdmaa.ui.base.ItemsViewModel
import javax.inject.Inject

class ServiceItemsViewMode
@Inject constructor(appPrefsStorage: AppPrefsStorage, repo: DataRepo) :
    ItemsViewModel(appPrefsStorage, repo) {

}