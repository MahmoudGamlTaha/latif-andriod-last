package com.latifapp.latif.ui.auth.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import com.latifapp.latif.R
import com.latifapp.latif.data.local.PreferenceConstants.Companion.Lang_PREFS
import com.latifapp.latif.databinding.ActivityLoginBinding
import com.latifapp.latif.ui.auth.signup.SignUpActivity
import com.latifapp.latif.ui.base.BaseActivity
import com.latifapp.latif.ui.main.home.MainActivity
import com.latifapp.latif.utiles.Utiles
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_login.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity :BaseActivity<LoginViewModel,ActivityLoginBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        CoroutineScope(Dispatchers.Main).launch {
            appPrefsStorage.setValue(Lang_PREFS,"en")
            appPrefsStorage.getValueAsFlow(Lang_PREFS,"ar").collect {
                Utiles.log_D("msmsmsmsm",it)
            }

        }

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
            binding.usernameEx.setError(null)
            binding.passwordEx.setError(null)
            startActivity(Intent(this,SignUpActivity::class.java))
        }
        binding.loginBtn.setOnClickListener {
            //startActivity(Intent(this,MainActivity::class.java))
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