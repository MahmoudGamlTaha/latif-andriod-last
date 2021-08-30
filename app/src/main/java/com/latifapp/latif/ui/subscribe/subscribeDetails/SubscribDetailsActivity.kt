package com.latifapp.latif.ui.subscribe.subscribeDetails

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.latifapp.latif.R
import com.latifapp.latif.data.models.ExtraModel
import com.latifapp.latif.data.models.SubscribeModel
import com.latifapp.latif.databinding.ActivitySubscribDetailsBinding
import com.latifapp.latif.ui.base.BaseActivity
import com.latifapp.latif.ui.details.ExtraAdapter
import com.latifapp.latif.utiles.MyContextWrapper
import com.latifapp.latif.utiles.Utiles
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@AndroidEntryPoint
class SubscribDetailsActivity : BaseActivity<SubscribeDetailsViewModel, ActivitySubscribDetailsBinding>() {


    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(MyContextWrapper.wrap(newBase, Utiles.LANGUAGE))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.backBtn.setOnClickListener {
            onBackPressed()
        }
        viewModel.getDetails(intent.extras?.getString("id") ?: "").observe(this, Observer {
            if (it != null) {
                binding.name.setText(if (isEnglish) it.name else it.nameAr)
                binding.descriptionTxt.setText(if (isEnglish) it.description else it.descriptionAr)

                setList(it)
                binding.container.visibility=VISIBLE
            }
        })
    }

    private fun setList(it: SubscribeModel) {
        val extra: MutableList<ExtraModel> = mutableListOf()
        extra.add(
            ExtraModel(
                name = getString(R.string.ads_numbers),
                name_ar = getString(R.string.ads_numbers),
                value = it.adsNumber,
                value_ar = it.adsNumber
            )
        )
        extra.add(
            ExtraModel(
                name = getString(R.string.periodInDays),
                name_ar = getString(R.string.periodInDays),
                value = "${it.periodInDays} ${it.periodUnit}",
                value_ar = "${it.periodInDays} ${it.periodUnit}"
            )
        )
        extra.add(
            ExtraModel(
                name = getString(R.string.users),
                name_ar = getString(R.string.users),
                value = it.numberUser,
                value_ar = it.numberUser
            )
        )
        extra.add(
            ExtraModel(
                name = getString(R.string.price),
                name_ar = getString(R.string.price),
                value = it.price,
                value_ar = it.price
            )
        )
//        extra.add(
//            ExtraModel(
//                name = getString(R.string.periodUnit),
//                name_ar = getString(R.string.periodUnit),
//                value = it.periodUnit,
//                value_ar = it.periodUnit
//            )
//        )

        binding.extraList.apply {
            layoutManager = GridLayoutManager(this@SubscribDetailsActivity, 2)
            adapter = ExtraAdapter(extra, viewModel.lang.equals("en"))
        }
    }

    override fun setBindingView(inflater: LayoutInflater): ActivitySubscribDetailsBinding {
        return ActivitySubscribDetailsBinding.inflate(inflater)
    }

    override fun showLoader() {
        binding.loader.bar.visibility = View.VISIBLE
    }

    override fun hideLoader() {
        binding.loader.bar.visibility = View.GONE
    }
}