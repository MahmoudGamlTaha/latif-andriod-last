package com.latifapp.latif.ui.fav

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.latifapp.latif.R
import com.latifapp.latif.data.models.AdsModel
import com.latifapp.latif.databinding.ActivitySubscribBinding
import com.latifapp.latif.ui.base.BaseActivity
import com.latifapp.latif.ui.details.DetailsActivity
import com.latifapp.latif.ui.main.petsList.PetsListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class FavActivity : BaseActivity<FavViewModel, ActivitySubscribBinding>() {

    private val adapter_ = PetsListAdapter()
    private var isLoadingData = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.toolbar.title.text = getString(R.string.favourite)
        binding.toolbar.backBtn.setOnClickListener {
            onBackPressed()
        }
        setlist()
        getSubscribList()
    }

    private fun getSubscribList() {
        lifecycleScope.launchWhenStarted {
            viewModel.getFavAds().collect {
                if (!it.isNullOrEmpty()) {
                    adapter_.list = it as MutableList<AdsModel>
                    isLoadingData = false
                }
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
                getSubscribList()
            }
        }
    }

    private fun setlist() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@FavActivity)
            adapter = adapter_
            addOnScrollListener(scrollListener)
        }
        adapter_.action = object : PetsListAdapter.Action {
            override fun onAdClick(id: Int?) {
                val intent = Intent(this@FavActivity, DetailsActivity::class.java)
                intent.putExtra("ID", id)
                startActivity(intent)
            }
        }
    }

    override fun setBindingView(inflater: LayoutInflater): ActivitySubscribBinding {
        return ActivitySubscribBinding.inflate(inflater)
    }

    override fun showLoader() {
        binding.loader.bar.visibility = View.VISIBLE
    }

    override fun hideLoader() {
        binding.loader.bar.visibility = View.GONE
    }
}