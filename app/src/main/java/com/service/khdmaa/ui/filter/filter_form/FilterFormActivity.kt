package com.service.khdmaa.ui.filter.filter_form

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.service.khdmaa.R
import com.service.khdmaa.databinding.ActivitySellBinding
import com.service.khdmaa.ui.base.BaseActivity
import com.service.khdmaa.ui.filter.filter_list.FilterActivity
import com.service.khdmaa.ui.sell.CreationFormFragment
import com.service.khdmaa.ui.sell.SellViewModel
import com.service.khdmaa.ui.sell.views.*
import com.service.khdmaa.utiles.MyContextWrapper
import com.service.khdmaa.utiles.Utiles

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.io.Serializable

@AndroidEntryPoint
class FilterFormActivity : BaseActivity<SellViewModel, ActivitySellBinding>() {

    private var isMap: Boolean? = true
    private var type: String? = ""
    private var filterHasMap: Boolean = false
    private val fragment_ = CreationFormFragment()

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(MyContextWrapper.wrap(newBase, Utiles.LANGUAGE))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utiles.setLocalization(this, lang)
        binding.backBtn.setOnClickListener { onBackPressed() }
        binding.title.setText(R.string.filter)
        binding.submitBtn.setText(R.string.filter)
        type = intent.extras?.getString("type")
        isMap = intent.extras?.getBoolean("isMap")

        viewModel.isSellAction = false

        binding.submitBtn.setOnClickListener {
            viewModel.submit()
        }
        setFragment(fragment_)
        fragment_.getForm(type)
        lifecycleScope.launchWhenStarted {
            viewModel.responseOfSubmit.observe(this@FilterFormActivity, Observer {
                if (!it.msg.isNullOrEmpty()) {
                    toastMsg_Success(it.msg, binding.root, this@FilterFormActivity)

                }
                onBackPressed()
            })
        }

        binding.submitBtn.setOnClickListener {
            viewModel.submit()

        }

        lifecycleScope.launchWhenStarted {
            viewModel.hashMapFlow.observe(this@FilterFormActivity, Observer {
                Utiles.log_D("dmdmdndkdkdkdkd", it)
                if (it != null) {
                    submitAdForm(it)
                    viewModel.clearFilter();
                }

            })
        }
        lifecycleScope.launchWhenStarted {
            viewModel.submitBtnVisible.collect {
                if (it)
                    binding.submitBtn.visibility = View.VISIBLE
            }
        }
    }

    fun setFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.formContainer, fragment)
        transaction.commit()
    }


    //
    private fun submitAdForm(hashMap: MutableMap<String, Any>) {
        if (hashMap.isNullOrEmpty())
            toastMsg_Warning(getString(R.string.addFilterValue), binding.root, this)
//        else if (lat == null || lat == 0.0) {
//            if (filterHasMap)
//                toastMsg_Warning(getString(R.string.plz_add_location), binding.root, this)
//            else {
//                lat = PetsFragment.Latitude_
//                lng = PetsFragment.Longitude_
//                setLocation()
//            }
//        }

        else {
            val intent = Intent(this, FilterActivity::class.java)
            intent.putExtra("url", viewModel.url)
            intent.putExtra("type", type)
            intent.putExtra("isMaping", isMap)
            intent.putExtra("hashMap", hashMap as Serializable)
            startActivity(intent)
        }
    }

    override fun setBindingView(inflater: LayoutInflater): ActivitySellBinding {
        return ActivitySellBinding.inflate(inflater)
    }

    override fun showLoader() {
        binding.loader.bar.visibility = View.VISIBLE
    }

    override fun hideLoader() {

        binding.loader.bar.visibility = View.GONE
    }
//

}