package com.latifapp.latif.ui.main.items

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
import com.latifapp.latif.ui.main.home.MainViewModel
import com.latifapp.latif.ui.main.petsList.PetsListAdapter
import com.latifapp.latif.ui.sell.SellActivity
import com.latifapp.latif.utiles.Utiles
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ItemsFragment : BaseFragment<MainViewModel, FragmentPetsListBinding>() {


    private var category: Int? = null
    private var isLoadingData = false
    private lateinit var adapter: ItemsAdapter
    private var categoryType = 0
    private var itemsType = ""
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!::adapter.isInitialized) {
            setList()

            binding.sellBtn.setOnClickListener {
                startActivity(Intent(activity, SellActivity::class.java))
            }
        }
    }

    private fun setList() {
        adapter = ItemsAdapter()
        binding.petsListRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@ItemsFragment.adapter
            addOnScrollListener(scrollListener)
        }
        adapter.action = object : PetsListAdapter.Action {
            override fun onAdClick(id: Int?) {
                val intent = Intent(activity, DetailsActivity::class.java)
                intent.putExtra("ID", id)
                startActivity(intent)
            }
        }
        getTypeOfCategorAndItems()
    }

    private fun getTypeOfCategorAndItems() {
        lifecycleScope.launchWhenStarted {
            viewModel.typeFlow.collect {
                categoryType = it.categoryType
                itemsType = it.itemType
                Utiles.log_D("nvnvnvnvnvnvnnv",itemsType)
                selectedCategory(-1)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.categoryFlow.collect {
                selectedCategory(it)
            }
        }
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
            viewModel.getItems(itemsType, category).collect {
                if (it != null) {
                    adapter.list = it as MutableList<AdsModel>
                    if (it.isNotEmpty())
                        isLoadingData = false
                }
            }
        }
    }


    override fun setBindingView(inflater: LayoutInflater): FragmentPetsListBinding {
        return FragmentPetsListBinding.inflate(inflater, null, false)
    }


    override fun showLoader() {
        binding.loader.bar.visibility = View.VISIBLE
    }

    override fun hideLoader() {
        binding.loader.bar.visibility = View.GONE
    }

    private fun selectedCategory(id: Int) {
        category = if (id == -1) null else id
        adapter.list.clear()
        viewModel.page = 0
        getPetsList()
    }
}