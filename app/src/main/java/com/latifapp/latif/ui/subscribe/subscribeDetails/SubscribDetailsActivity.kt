package com.latifapp.latif.ui.subscribe.subscribeDetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.latifapp.latif.R
import com.latifapp.latif.data.models.ExtraModel
import com.latifapp.latif.data.models.SubscribeModel
import com.latifapp.latif.databinding.ActivitySubscribDetailsBinding
import com.latifapp.latif.ui.base.BaseActivity
import com.latifapp.latif.ui.details.ExtraAdapter
import com.latifapp.latif.ui.sell.SellViewModel
import com.latifapp.latif.ui.subscribe.subscribList.SubscribeViewModel
import com.latifapp.latif.ui.subscribe.subscribeDetails.dialog.PaymentTypeDialogFragment
import com.latifapp.latif.utiles.MyContextWrapper
import com.latifapp.latif.utiles.Utiles
import com.oppwa.mobile.connect.checkout.dialog.CheckoutActivity
import com.oppwa.mobile.connect.checkout.meta.CheckoutSettings
import com.oppwa.mobile.connect.exception.PaymentError
import com.oppwa.mobile.connect.provider.Connect
import com.oppwa.mobile.connect.provider.Transaction
import com.oppwa.mobile.connect.provider.TransactionType
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SubscribDetailsActivity : BaseActivity<SubscribeDetailsViewModel, ActivitySubscribDetailsBinding>() {
    override val viewModel by viewModels<SubscribeDetailsViewModel>()
    val dialog=PaymentTypeDialogFragment()
    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(MyContextWrapper.wrap(newBase, Utiles.LANGUAGE))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val id =intent.extras?.getString("id") ?: ""
        binding.backBtn.setOnClickListener {
            onBackPressed()
        }
        viewModel.getDetails(id).observe(this, Observer {
            if (it != null) {
                binding.name.setText(if (isEnglish) it.name else it.nameAr)
                binding.descriptionTxt.setText(if (isEnglish) it.description else it.descriptionAr)

                setList(it)
                binding.container.visibility = VISIBLE
            }
        })
        dialog.action= object :PaymentTypeDialogFragment.PaymentAction{
            override fun setpayment(type: String?) {
                if (viewModel.token.isNullOrEmpty()) {
                    toastMsg_Warning(getString(R.string.loginFirst), binding.root, this@SubscribDetailsActivity)
                } else
                viewModel.subscribe(id,type)
            }
        }


        binding.subscribeBtn.setOnClickListener {
            dialog.show(supportFragmentManager, "paymentDialog")

        }


        viewModel.liveData.observe(this, Observer {
            if (it != null) {
                intentToCheckout(
                    it.second,HashSet(it.first)
                )
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
                value = it.periodInDays,
                value_ar = it.periodInDays
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

    fun intentToCheckout(checkoutId: String, paymentBrands: MutableSet<String>) {
       // paymentBrands.add("MADA")
         val checkoutSettings = CheckoutSettings(
             checkoutId,
             paymentBrands,
             Connect.ProviderMode.LIVE
         )
        checkoutSettings.shopperResultUrl = "com.latifapp.latif://result"

        try {
            val intent = checkoutSettings.createCheckoutActivityIntent(this)

            startActivityForResult(intent, CheckoutActivity.REQUEST_CODE_CHECKOUT)
        }catch (e: Exception){
            Utiles.log_D("checkoutSettingserror", checkoutId)
        }
        Utiles.log_D("checkoutSettings0", checkoutId)
     }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (intent.scheme == "devsupport") {
            val checkoutId = intent.data!!.getQueryParameter("id")
            toastMsg_Success(getString(R.string.successPayment), binding.root, this)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Utiles.log_D("checkoutSettings300", resultCode)
        when (resultCode) {
            CheckoutActivity.RESULT_OK -> {
                val transaction =
                    data?.getParcelableExtra<Transaction>(CheckoutActivity.CHECKOUT_RESULT_TRANSACTION)
                Utiles.log_D("checkoutSettings5", transaction)
                val resourcePath =
                    data?.getStringExtra(CheckoutActivity.CHECKOUT_RESULT_RESOURCE_PATH)
                Utiles.log_D("checkoutSettings6", resourcePath)

                if (transaction?.transactionType == TransactionType.SYNC) {
                    Utiles.log_D("checkoutSettings4", "success")
                    toastMsg_Success(getString(R.string.successPayment), binding.root, this)
                } else {
                    /* wait for the asynchronous transaction callback in the onNewIntent() */
                    Utiles.log_D("checkoutSettings6", " np")
                }
            }
            CheckoutActivity.RESULT_CANCELED -> {
                Utiles.log_D("checkoutSettings7", "canceled")
                toastMsg_Warning(getString(R.string.cancelPayment), binding.root, this)
            }
            CheckoutActivity.RESULT_ERROR -> {
                val error =
                    data?.getParcelableExtra<PaymentError>(CheckoutActivity.CHECKOUT_RESULT_ERROR)
                Utiles.log_D("checkoutSettings3", error?.errorMessage)
                Utiles.log_D("checkoutSettings3", error?.errorInfo)
                Utiles.log_D("checkoutSettings3", error?.errorCode)
                toastMsg_Warning(getString(R.string.errorPayment), binding.root, this)
            }
            else -> {
            }
        }
    }


}