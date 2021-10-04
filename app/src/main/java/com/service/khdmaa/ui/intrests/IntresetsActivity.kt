package com.service.khdmaa.ui.intrests

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.service.khdmaa.R
import com.service.khdmaa.data.models.CategoryModel
import com.service.khdmaa.databinding.ActivityIntresetsBinding
import com.service.khdmaa.ui.auth.signup.fragments.interests.InterestsAdapter
import com.service.khdmaa.ui.base.BaseActivity
import com.service.khdmaa.utiles.MyContextWrapper
import com.service.khdmaa.utiles.Utiles
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntresetsActivity : BaseActivity<IntrestsViewModel,ActivityIntresetsBinding>() {
    private var isLoadingData: Boolean=true
    private val adapter_: InterestsAdapter = InterestsAdapter()
    private var index=0
    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(MyContextWrapper.wrap(newBase, Utiles.LANGUAGE))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utiles.setLocalization(this, lang)
        binding.recyclerView.apply {
            layoutManager= GridLayoutManager(this@IntresetsActivity,3)
            adapter=adapter_
            adapter_.lang=viewModel.lang
            addOnScrollListener(scrollListener)
        }

        getList()
        getMyCategories()

        binding.backBtn.setOnClickListener {
            onBackPressed()
        }
        binding.doneBtn.setOnClickListener {

                val interestList=adapter_.selectedList.toMutableList()
                viewModel.setInterst(interestList).observe(this, Observer {
                    if (it) {
                        onBackPressed()
                    }
                })

            Utiles.log_D("xmmxmxmmxmxmx",adapter_.selectedList)
        }
     }

    private fun getMyCategories() {
        viewModel.getMyCategories().observe(this, Observer {
            it?.apply {
                val list= map { model-> model.id }
                adapter_.selectedList.addAll(list)
                adapter_.notifyDataSetChanged()
            }

        })
    }

    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val totalItemCount = layoutManager?.itemCount
            if (!isLoadingData && totalItemCount <= layoutManager.findLastVisibleItemPosition() + 2) {
                isLoadingData = true

                getList()
            }
        }


    }
    private fun getList() {
        viewModel.getAllCategories(index).observe(this, Observer {
             if (!it.isNullOrEmpty()) {
                 adapter_.list.clear()
                 adapter_.list = it as MutableList<CategoryModel>
                isLoadingData = false
                 index++
            }
        })
    }
    override fun setBindingView(inflater: LayoutInflater): ActivityIntresetsBinding {
         return ActivityIntresetsBinding.inflate(inflater)
    }
    override fun showLoader() {
        binding.loader.bar.visibility = View.VISIBLE
    }

    override fun hideLoader() {
        binding.loader.bar.visibility = View.GONE
    }
}