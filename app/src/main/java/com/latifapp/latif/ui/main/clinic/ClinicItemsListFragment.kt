package com.latifapp.latif.ui.main.clinic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.latifapp.latif.data.models.AdsModel
import com.latifapp.latif.databinding.FragmentServicesBinding
import com.latifapp.latif.ui.base.BaseFragment
import com.latifapp.latif.ui.main.petsList.PetsListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ClinicItemsListFragment :BaseFragment<ClinicItemsViewMode, FragmentServicesBinding>() {
    private var isLoadingData = false
    private lateinit var adapter: PetsListAdapter
    private var category: Int? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         category=arguments?.getInt("category")
        if (!::adapter.isInitialized) {
            setList()

        }


    }

    private fun setList() {

        adapter = PetsListAdapter()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@ClinicItemsListFragment.adapter
            addOnScrollListener(scrollListener)
        }
       getClinicList()
    }


    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val totalItemCount = layoutManager?.itemCount
            if (!isLoadingData && totalItemCount <= layoutManager.findLastVisibleItemPosition() + 2) {
                isLoadingData = true
                getClinicList()
            }
        }
    }

    private fun getClinicList() {
        lifecycleScope.launchWhenStarted {
            viewModel.getItems("PET_CARE", category).collect {
                if (it != null) {
                    adapter.list = it as MutableList<AdsModel>
                    if (it.isNotEmpty())
                        isLoadingData = false
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