package com.latifapp.latif.ui.filter.filter_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.latifapp.latif.R
import com.latifapp.latif.databinding.ActivityFilterBinding
import com.latifapp.latif.ui.base.BaseActivity
import com.latifapp.latif.ui.filter.filter_list.fragment.FilterListFragment
import com.latifapp.latif.ui.filter.filter_list.fragment.FilterMapFragment
import com.latifapp.latif.utiles.Utiles
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FilterActivity : BaseActivity<FilterListViewModel, ActivityFilterBinding>() {
    private var url: String? = ""
    private var type: String? = ""
    private var isMaping: Boolean = true
    private var hashMap: MutableMap<String, Any> = mutableMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.backBtn.setOnClickListener {
            onBackPressed()
        }
        url = intent.extras?.getString("url")
        type = intent.extras?.getString("type")
        isMaping = intent.extras?.getBoolean("isMaping")?: true
        hashMap = intent.extras?.getSerializable("hashMap") as MutableMap<String, Any>
        Utiles.log_D("cncncnncncncncn", "${isMaping } 0000 $type")
        getFilter()
        if (isMaping )
            setFragment(FilterMapFragment())
        else setFragment(FilterListFragment())
    }

    private fun getFilter() {

        lifecycleScope.launchWhenStarted {
            viewModel.filter(url!!, hashMap, type).observe(this@FilterActivity, Observer {
                if (!it.response.data.isNullOrEmpty()) {

                } else toastMsg_Warning(
                    getString(R.string.noAds),
                    binding.root,
                    this@FilterActivity
                )

            })
        }
    }


    fun setFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.framelayout, fragment)
        transaction.commit()
    }

    override fun setBindingView(inflater: LayoutInflater): ActivityFilterBinding {
        return ActivityFilterBinding.inflate(inflater)
    }

    override fun showLoader() {
        binding.loader.bar.visibility = View.VISIBLE
    }

    override fun hideLoader() {
        binding.loader.bar.visibility = View.GONE
    }
}