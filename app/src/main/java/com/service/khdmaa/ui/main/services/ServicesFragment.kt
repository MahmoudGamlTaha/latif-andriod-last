package com.service.khdmaa.ui.main.services

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.service.khdmaa.R
import com.service.khdmaa.databinding.FragmentServicesBinding
import com.service.khdmaa.ui.base.BaseFragment
import com.service.khdmaa.ui.main.pets.PetsAdapter
import com.service.khdmaa.ui.main.pets.PetsViewModel
import com.service.khdmaa.utiles.AppConstants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class ServicesFragment : BaseFragment<PetsViewModel, FragmentServicesBinding>() {


    private val adapter_ = ServiceAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupList()
    }

    val selectService=object :PetsAdapter.CategoryActions{
        override fun selectedCategory(id: Int) {
            val bundle =Bundle()
            bundle.putInt("category",id!!)
            navController.navigate(R.id.nav_services_list_fragments,bundle)
        }

    }
    private fun setupList() {
        adapter_.action=selectService
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = adapter_
        }

        lifecycleScope.launchWhenStarted {
            viewModel.getCategoriesList(AppConstants.SERVICE).collect {
                if (!it.isNullOrEmpty()) {
                    adapter_.list = (it)

                }
            }
        }
    }

    override fun setBindingView(inflater: LayoutInflater): FragmentServicesBinding {
        return FragmentServicesBinding.inflate(inflater)
    }


    override fun showLoader() {
        binding.loader.bar.visibility = View.VISIBLE
    }

    override fun hideLoader() {
        binding.loader.bar.visibility = View.GONE
    }
}