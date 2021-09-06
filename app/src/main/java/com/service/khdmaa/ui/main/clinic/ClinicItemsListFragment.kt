package com.service.khdmaa.ui.main.clinic

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.service.khdmaa.data.models.AdsModel
import com.service.khdmaa.databinding.FragmentServicesBinding
import com.service.khdmaa.ui.base.BaseFragment
import com.service.khdmaa.ui.details.DetailsActivity
import com.service.khdmaa.ui.main.items.PetsListAdapter
import com.service.khdmaa.utiles.AppConstants
import com.service.khdmaa.utiles.Utiles
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
    override fun onStart() {
        super.onStart()
        activity?.let { Utiles.setLocalization(it, lang) }
    }
    private fun setList() {

        adapter = PetsListAdapter()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@ClinicItemsListFragment.adapter
            addOnScrollListener(scrollListener)
        }
        adapter.action= object : PetsListAdapter.Action{
            override fun onAdClick(id: Int?) {
                val intent = Intent(activity, DetailsActivity::class.java)
                intent.putExtra("ID",id)
                startActivity(intent)
            }
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
            viewModel.getItems(AppConstants.PET_CARE_STR, category).collect {
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