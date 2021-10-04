package com.service.khdmaa.ui.main.clinic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.service.khdmaa.R
import com.service.khdmaa.databinding.FragmentClinicBinding
import com.service.khdmaa.ui.base.BaseFragment
import com.service.khdmaa.ui.main.pets.PetsAdapter
import com.service.khdmaa.ui.main.services.ServiceViewModel
import com.service.khdmaa.utiles.AppConstants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ClinicFragment : BaseFragment<ServiceViewModel, FragmentClinicBinding>() {

    private lateinit var clinicAdapter: ClinicAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!::clinicAdapter.isInitialized) {
            clinicAdapter = ClinicAdapter()
            clinicAdapter.action = selectPetCare
            binding.recyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = clinicAdapter

            }


            getCategories()


        }
    }

    val selectPetCare = object : PetsAdapter.CategoryActions {
        override fun selectedCategory(id: Int) {
            val bundle = Bundle()
            bundle.putInt("category", id!!)
            navController.navigate(R.id.nav_clinic_list_fragments, bundle)
        }

    }

    private fun getCategories() {
        lifecycleScope.launchWhenStarted {
            viewModel.getCategoriesList(AppConstants.OCCASIONAL).collect {
                if (!it.isNullOrEmpty()) {
                    clinicAdapter.list = (it)

                }
            }
        }
    }

    override fun setBindingView(inflater: LayoutInflater): FragmentClinicBinding {
        return FragmentClinicBinding.inflate(inflater, null, false)
    }

    override fun showLoader() {
        binding.loader.bar.visibility = View.VISIBLE
    }

    override fun hideLoader() {
        binding.loader.bar.visibility = View.GONE
    }

}