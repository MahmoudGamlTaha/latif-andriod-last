package com.latifapp.latif.ui.main.services

import com.latifapp.latif.data.local.AppPrefsStorage
import com.latifapp.latif.network.repo.DataRepo
import com.latifapp.latif.ui.base.BaseViewModel
import com.latifapp.latif.ui.base.ItemsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ServiceItemsViewMode
@Inject constructor(appPrefsStorage: AppPrefsStorage, repo: DataRepo) :
    ItemsViewModel(appPrefsStorage, repo) {

}