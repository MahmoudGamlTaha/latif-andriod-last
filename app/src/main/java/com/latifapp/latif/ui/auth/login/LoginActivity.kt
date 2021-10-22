package com.latifapp.latif.ui.auth.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.latifapp.latif.R
import com.latifapp.latif.data.local.AppPrefsStorage
import com.latifapp.latif.data.local.PreferenceConstants.Companion.Lang_PREFS
import com.latifapp.latif.databinding.ActivityLoginBinding
import com.latifapp.latif.network.repo.DataRepo
import com.latifapp.latif.ui.auth.signup.SignUpActivity
import com.latifapp.latif.ui.base.BaseActivity
import com.latifapp.latif.ui.filter.filter_list.FilterListViewModel
import com.latifapp.latif.ui.main.home.MainActivity
import com.latifapp.latif.utiles.Utiles
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_login.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : BaseActivity<LoginViewModel, ActivityLoginBinding>() {
    override val viewModel by viewModels<LoginViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utiles.setLocalization(this, lang)
        Utiles.log_D("xmmxmxmmxmxmssx", viewModel)

        viewModel.validateLiveData.observe(this, Observer {
            if (it !=null){
                when(it){
                    LoginViewModel.SignUpFiled.email_->
                        binding.usernameEx.setError(getString(R.string.usernameError))
                    LoginViewModel.SignUpFiled.password_->
                        binding.passwordEx.setError(getString(R.string.passwordError))
                }
            }
        })
        binding.signUpBtn.setOnClickListener {

            startActivity(Intent(this,SignUpActivity::class.java))
        }

        viewModel.successInputs.observe(this, Observer {
            if (it){
                startActivity(Intent(this,MainActivity::class.java))
                finishAffinity()
            }
        })
        viewModel.errorIputsMsg.observe(this, Observer {
            if (it!=null){
                toastMsg_Warning(getString(it), binding.root, this@LoginActivity)
            }
        })
        binding.loginBtn.setOnClickListener {
            //
            binding.usernameEx.setError(null)
            binding.passwordEx.setError(null)
            viewModel.login(binding.usernameEx.text.toString(),
                binding.passwordEx.text.toString())
        }
     }

    override fun setBindingView(inflater: LayoutInflater): ActivityLoginBinding {
        return  ActivityLoginBinding.inflate(inflater)

    }


    override fun showLoader() {
         binding.loader.bar.visibility = View.VISIBLE
    }

    override fun hideLoader() {
        binding.loader.bar.visibility = View.GONE
    }
}