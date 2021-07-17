package com.latifapp.latif.ui.base

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.latifapp.latif.data.local.AppPrefsStorage
import com.latifapp.latif.data.local.PreferenceConstants
import com.latifapp.latif.ui.auth.login.LoginActivity
import com.latifapp.latif.ui.filter.filter_list.FilterListViewModel
import com.latifapp.latif.utiles.Utiles
import com.latifapp.latif.utiles.Utiles.setLocalization
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.reflect.KClass

open abstract class BaseActivity<viewmodel : BaseViewModel, viewbind : ViewBinding>():AppCompatActivity(),BaseView<viewbind> {
    @Inject
    lateinit var viewModel: viewmodel
     @Inject
    lateinit var appPrefsStorage: AppPrefsStorage
    public lateinit var binding: viewbind
    val lang
    get() = viewModel.lang

    val token
    get() = viewModel.token
    val isEnglish
        get() = lang.equals("en")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utiles.log_D("nffnnnfnfnf22","${lang=="en"}  ....$lang")
        setLocalization(this,lang)
        binding = setBindingView(layoutInflater)
        setContentView(binding.root)


            viewModel.errorMsg_.observe(this@BaseActivity, Observer {
                if (!it.isNullOrEmpty())
                // Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    toastMsg_Warning(it, binding.root, this@BaseActivity)
            })

            viewModel.loginAgain_.observe(this@BaseActivity, Observer {
                if (it !=null && it)
                startActivity(Intent(this,LoginActivity::class.java))
            })


         lifecycleScope.launchWhenStarted {
            withContext(Dispatchers.Main) {
                viewModel.loader_.collect {
                    if (it)
                        showLoader()
                    else hideLoader()
                }

            }
        }

    }

    override fun onStart() {
        super.onStart()
        setLocalization(this,lang)
    }

    override fun onDestroy() {
        super.onDestroy()

    }
}