package com.latifapp.latif.ui.main.services

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.latifapp.latif.data.models.AdsModel
import com.latifapp.latif.databinding.FragmentServicesBinding
import com.latifapp.latif.ui.auth.signup.SignUpViewModel
import com.latifapp.latif.ui.base.BaseFragment
import com.latifapp.latif.ui.details.DetailsActivity
import com.latifapp.latif.ui.main.items.PetsListAdapter
import com.latifapp.latif.utiles.AppConstants
import com.latifapp.latif.utiles.Utiles
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ServicesItemsListFragment :BaseFragment<ServiceItemsViewMode, FragmentServicesBinding>() {
    override val viewModel by activityViewModels<ServiceItemsViewMode>()
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
            adapter = this@ServicesItemsListFragment.adapter
            addOnScrollListener(scrollListener)
        }
        adapter.action= object : PetsListAdapter.Action{
            override fun onAdClick(id: Int?) {
                val intent = Intent(activity, DetailsActivity::class.java)
                intent.putExtra("ID",id)
                startActivity(intent)
            }
        }
       getServiceList()
    }


    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val totalItemCount = layoutManager?.itemCount
            if (!isLoadingData && totalItemCount <= layoutManager.findLastVisibleItemPosition() + 2) {
                isLoadingData = true
                getServiceList()
            }
        }
    }

    private fun getServiceList() {
        lifecycleScope.launchWhenStarted {
            viewModel.getItems(AppConstants.SERVICE_STR, category).collect {
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