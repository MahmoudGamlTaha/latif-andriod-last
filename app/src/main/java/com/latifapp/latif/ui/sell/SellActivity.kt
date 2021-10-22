package com.latifapp.latif.ui.sell

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.latifapp.latif.R
import com.latifapp.latif.databinding.ActivitySellBinding
import com.latifapp.latif.ui.base.BaseActivity
import com.latifapp.latif.ui.main.blogs.blogsDetails.BolgDetailsViewModel
import com.latifapp.latif.ui.sell.views.*
import com.latifapp.latif.utiles.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_sell.*
import kotlinx.coroutines.flow.collect
import java.util.*

@AndroidEntryPoint
class SellActivity : BaseActivity<SellViewModel, ActivitySellBinding>() {

    override val viewModel by viewModels<SellViewModel>()
    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(MyContextWrapper.wrap(newBase, Utiles.LANGUAGE))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utiles.setLocalization(this, lang)
        binding.backBtn.setOnClickListener { onBackPressed() }
        viewModel.isSellAction=true

          binding.submitBtn.setOnClickListener {
              viewModel.submit()
         }
        setFragment(CreationFormFragment())
        lifecycleScope.launchWhenStarted {
            viewModel.responseOfSubmit.observe(this@SellActivity, Observer {
                if (!it.msg.isNullOrEmpty()) {
                    toastMsg_Success(it.msg, binding.root, this@SellActivity)

                }
                onBackPressed()
            })
        }
        lifecycleScope.launchWhenStarted {
            viewModel.submitBtnVisible.collect {
                if (it)
                    binding.submitBtn.visibility=View.VISIBLE
            }
        }
    }


    fun setFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.formContainer, fragment)
        transaction.commit()
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


}