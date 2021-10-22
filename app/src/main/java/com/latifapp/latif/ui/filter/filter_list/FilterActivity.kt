package com.latifapp.latif.ui.filter.filter_list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.latifapp.latif.R
import com.latifapp.latif.databinding.ActivityFilterBinding
import com.latifapp.latif.ui.base.BaseActivity
import com.latifapp.latif.ui.filter.filter_form.FilterViewModel
import com.latifapp.latif.ui.filter.filter_list.fragment.FilterListFragment
import com.latifapp.latif.ui.filter.filter_list.fragment.FilterMapFragment
import com.latifapp.latif.ui.sell.SellViewModel
import com.latifapp.latif.utiles.MyContextWrapper
import com.latifapp.latif.utiles.Utiles
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FilterActivity : BaseActivity<FilterListViewModel, ActivityFilterBinding>() {
    override val viewModel by viewModels<FilterListViewModel>()
    private var url: String? = ""
    private var type: String? = ""
    private var isMaping: Boolean = true
    private var hashMap: MutableMap<String, Any> = mutableMapOf()
    private var mapIsetup = false
    private var listIsetup = false
    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(MyContextWrapper.wrap(newBase, Utiles.LANGUAGE))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utiles.setLocalization(this, lang)

        binding.backBtn.setOnClickListener {
            onBackPressed()
        }
        url = intent.extras?.getString("url")
        type = intent.extras?.getString("type")
        isMaping = intent.extras?.getBoolean("isMaping") ?: true
        hashMap = intent.extras?.getSerializable("hashMap") as MutableMap<String, Any>
        Utiles.log_D("cncncnncncncncn", "${isMaping} 0000 $type")
        getFilter()

        setFilterFragments()

        binding.mapBtn.setOnClickListener {
            if (!isMaping) {
                isMaping = true
                setFilterFragments()
            }
        }
        binding.listeBtn.setOnClickListener {
            if (isMaping) {
                isMaping = false
                setFilterFragments()
            }
        }
    }

    private fun setFilterFragments() {
        if (isMaping) {
            if (!mapIsetup)
                setFragment(FilterMapFragment(), R.id.framelayoutMap)
            mapIsetup = true
            setScaleView(binding.mapBtn, binding.listeBtn)
            binding.framelayoutMap.visibility = View.VISIBLE
            binding.framelayoutList.visibility = View.GONE

        } else {
            if (!listIsetup)
                setFragment(FilterListFragment(), R.id.framelayoutList)
            listIsetup = true
            setScaleView(binding.listeBtn, binding.mapBtn)
            binding.framelayoutMap.visibility = View.GONE
            binding.framelayoutList.visibility = View.VISIBLE
        }
    }

    fun setScaleView(largeIcon: ImageView, smallView: ImageView) {
        largeIcon.scaleX = 1.2f
        largeIcon.scaleY = 1.2f
        smallView.scaleX = 0.6f
        smallView.scaleY = 0.6f
    }

    private fun getFilter() {

        lifecycleScope.launchWhenStarted {
            viewModel.filter("$url", hashMap, type).observe(this@FilterActivity, Observer {
                if (!it.response.data.isNullOrEmpty()) {

                } else toastMsg_Warning(
                    getString(R.string.noAds),
                    binding.root,
                    this@FilterActivity
                )

            })
        }
    }


    fun setFragment(fragment: Fragment, res: Int) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(res, fragment)
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