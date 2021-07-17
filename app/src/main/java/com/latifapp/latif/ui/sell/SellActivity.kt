package com.latifapp.latif.ui.sell

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.latifapp.latif.R
import com.latifapp.latif.data.models.AdsTypeModel
import com.latifapp.latif.data.models.RequireModel
import com.latifapp.latif.data.models.ShowConditionModel
import com.latifapp.latif.databinding.ActivitySellBinding
import com.latifapp.latif.ui.base.BaseActivity
import com.latifapp.latif.ui.main.pets.PetsFragment
import com.latifapp.latif.ui.map.MapsActivity
import com.latifapp.latif.ui.sell.adapters.ImagesAdapter
import com.latifapp.latif.ui.sell.views.*
import com.latifapp.latif.utiles.*
import com.latifapp.latif.utiles.Permissions.MapRequest
import com.latifapp.latif.utiles.Permissions.galleryRequest
import com.nguyenhoanglam.imagepicker.model.Config
import com.nguyenhoanglam.imagepicker.model.Image
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_sell.*
import kotlinx.coroutines.flow.collect
import java.io.File
import java.util.*

@AndroidEntryPoint
class SellActivity : BaseActivity<SellViewModel, ActivitySellBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.backBtn.setOnClickListener { onBackPressed() }
        viewModel.isSEllAction=true

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
    }


    fun setFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.formContainer, fragment)
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