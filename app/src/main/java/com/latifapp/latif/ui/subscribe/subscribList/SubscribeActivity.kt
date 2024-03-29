package com.latifapp.latif.ui.subscribe.subscribList

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.latifapp.latif.R
import com.latifapp.latif.data.models.SubscribeModel
import com.latifapp.latif.databinding.ActivitySubscribBinding
import com.latifapp.latif.ui.base.BaseActivity
import com.latifapp.latif.ui.sell.SellViewModel
import com.latifapp.latif.ui.subscribe.subscribeDetails.SubscribDetailsActivity
import com.latifapp.latif.utiles.MyContextWrapper
import com.latifapp.latif.utiles.Utiles
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SubscribeActivity : BaseActivity<SubscribeViewModel,ActivitySubscribBinding>() {
    override val viewModel by viewModels<SubscribeViewModel>()
    private var adapter_: SubscribeAdapter?=null
    private var isLoadingData  =false

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(MyContextWrapper.wrap(newBase, Utiles.LANGUAGE))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utiles.setLocalization(this, lang)
        binding.toolbar.title.text = getString(R.string.subscribe)
        binding.toolbar.backBtn.setOnClickListener {
            onBackPressed()
        }
        adapter_= SubscribeAdapter(isEnglish)
        adapter_?.action= object : SubscribeAdapter.SubscribeAction {
            override fun subscribeClick(id: String) {
                val intent =Intent(this@SubscribeActivity,SubscribDetailsActivity::class.java)
                intent.putExtra("id",id)
                 startActivity(intent)
            }

        }
        setlist()
        getSubscribList()
    }

    private fun getSubscribList() {
        lifecycleScope.launchWhenStarted {
            viewModel.getSubscribeList().collect {
                if (!it.isNullOrEmpty()) {
                    adapter_?.list = it as MutableList<SubscribeModel>
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
            layoutManager = LinearLayoutManager(this@SubscribeActivity)
            adapter = adapter_
            addOnScrollListener(scrollListener)
        }
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