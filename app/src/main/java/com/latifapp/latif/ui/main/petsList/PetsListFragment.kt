package com.latifapp.latif.ui.main.petsList

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.latifapp.latif.data.models.AdsModel
import com.latifapp.latif.databinding.FragmentPetsListBinding
import com.latifapp.latif.ui.base.BaseFragment
import com.latifapp.latif.ui.details.DetailsActivity
import com.latifapp.latif.ui.main.pets.PetsAdapter
import com.latifapp.latif.ui.main.pets.PetsViewModel
import com.latifapp.latif.ui.sell.SellActivity
import com.latifapp.latif.utiles.AppConstants
import com.latifapp.latif.utiles.AppConstants.PETS_STR
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class PetsListFragment : BaseFragment<PetsViewModel, FragmentPetsListBinding>() {
    private var category: Int? = null
    private var isLoadingData = false
    private lateinit var adapter: PetsListAdapter
    private val petsAdapter = PetsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!::adapter.isInitialized) {
            setList()
            getCategoriesList()

            binding.sellBtn.setOnClickListener {
                startActivity(Intent(activity, SellActivity::class.java))
            }
        }
    }

    private fun setList() {
        adapter = PetsListAdapter()
        binding.petsListRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@PetsListFragment.adapter
            addOnScrollListener(scrollListener)
        }
        adapter.action= object : PetsListAdapter.Action{
            override fun onAdClick(id: Int?) {
                val intent =Intent(activity,DetailsActivity::class.java)
                intent.putExtra("ID",id)
                startActivity(intent)
            }
        }
        getPetsList()
    }

    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val totalItemCount = layoutManager?.itemCount
            if (!isLoadingData && totalItemCount <= layoutManager.findLastVisibleItemPosition() + 2) {
                isLoadingData = true
                getPetsList()
            }
        }
    }

    private fun getPetsList() {

        lifecycleScope.launchWhenStarted {
            viewModel.getItems(PETS_STR, category).collect {
                if (it != null) {
                    adapter.list = it as MutableList<AdsModel>
                    if (it.isNotEmpty())
                        isLoadingData = false
                }
            }
        }
    }

    private fun getCategoriesList() {
        binding.petsCatgRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = this@PetsListFragment.petsAdapter
        }
        petsAdapter.action = object : PetsAdapter.CategoryActions {
            override fun selectedCategory(id: Int) {
                category = id
                adapter.list.clear()
                viewModel.page = 0
                getPetsList()
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.getCategoriesList(AppConstants.PETS).collect {
                if (!it.isNullOrEmpty()) {
                    petsAdapter.list.addAll(it)
                    petsAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    override fun setBindingView(inflater: LayoutInflater): FragmentPetsListBinding {
        return FragmentPetsListBinding.inflate(inflater)
    }


    override fun showLoader() {
        binding.loader.bar.visibility = View.VISIBLE
    }

    override fun hideLoader() {
        binding.loader.bar.visibility = View.GONE
    }


}