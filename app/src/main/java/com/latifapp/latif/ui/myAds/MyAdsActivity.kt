package com.latifapp.latif.ui.myAds

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.latifapp.latif.R
import com.latifapp.latif.data.models.AdsModel
import com.latifapp.latif.databinding.ActivitySubscribBinding
import com.latifapp.latif.ui.base.BaseActivity
import com.latifapp.latif.ui.details.DetailsActivity
import com.latifapp.latif.ui.main.items.PetsListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MyAdsActivity : BaseActivity<MyAdsViewModel,ActivitySubscribBinding>() {

    private val adapter_= MyAdsAdapter()
    private var isLoadingData  =false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.toolbar.title.text = getString(R.string.myAds)
        binding.toolbar.backBtn.setOnClickListener {
            onBackPressed()
        }
        setlist()
        getSubscribList()
    }

    private fun getSubscribList() {
        lifecycleScope.launchWhenStarted {
            viewModel.getMyAds().collect {
                if (!it.isNullOrEmpty()) {
                    adapter_.list = it as MutableList<AdsModel>
                    isLoadingData=false
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
            layoutManager = LinearLayoutManager(this@MyAdsActivity)
            adapter = adapter_
            addOnScrollListener(scrollListener)
        }
        adapter_.action_ = object : PetsListAdapter.Action, MyAdsAdapter.MyAdsAction {
            override fun activeAd(activeAd: Boolean, id: Int?, position: Int) {
                viewModel.activateAd(activeAd,id).observe(this@MyAdsActivity, Observer {
                    if (it!=null) {
                        var active=activeAd
                        if (!it)//failed
                            active=!activeAd
                        adapter_.switchActivation(active, position)
                    }
                })
            }

            override fun onAdClick(id: Int?) {
                val intent = Intent(this@MyAdsActivity, DetailsActivity::class.java)
                intent.putExtra("ID", id)
                startActivity(intent)
            }}
    }

    override fun setBindingView(inflater: LayoutInflater): ActivitySubscribBinding {
        return ActivitySubscribBinding.inflate(inflater)
    }

    override fun showLoader() {
        binding.loader.bar.visibility=VISIBLE
    }

    override fun hideLoader() {
        binding.loader.bar.visibility= GONE
    }
}