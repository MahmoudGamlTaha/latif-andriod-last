package com.service.khdmaa.ui.auth.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.service.khdmaa.R
import com.service.khdmaa.data.local.PreferenceConstants.Companion.Lang_PREFS
import com.service.khdmaa.databinding.ActivityLoginBinding
import com.service.khdmaa.ui.auth.signup.SignUpActivity
import com.service.khdmaa.ui.base.BaseActivity
import com.service.khdmaa.ui.main.home.MainActivity
import com.service.khdmaa.utiles.Utiles
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_login.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import net.openid.appauth.TokenRequest

@AndroidEntryPoint
class LoginActivity :BaseActivity<LoginViewModel,ActivityLoginBinding>() {
    lateinit var callbackManager: CallbackManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utiles.setLocalization(this, lang)
        /////
        callbackManager = CallbackManager.Factory.create()
        val loginButton = findViewById<LoginButton>(R.id.login_button)
        loginButton.setReadPermissions(listOf("public_profile", "email"))
        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
            override fun onSuccess(loginResult: LoginResult?) {
                Log.d("TAG", "Success Login")
                // Get User's Info
            }

            override fun onCancel() {
                Toast.makeText(this@LoginActivity, "Login Cancelled", Toast.LENGTH_SHORT)
            }

            override fun onError(exception: FacebookException) {
                Toast.makeText(this@LoginActivity, exception.message, Toast.LENGTH_LONG)
            }
        })
        ///

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