package com.latifapp.latif.ui.main.clinic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.latifapp.latif.R
import com.latifapp.latif.databinding.FragmentClinicBinding
import com.latifapp.latif.ui.base.BaseFragment
import com.latifapp.latif.ui.main.pets.PetsAdapter
import com.latifapp.latif.ui.main.services.ServiceViewModel
import com.latifapp.latif.utiles.AppConstants
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
            viewModel.getCategoriesList(AppConstants.PET_CARE).collect {
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