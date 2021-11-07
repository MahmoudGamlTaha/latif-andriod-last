package com.service.khdmaa.ui.auth.login

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.facebook.*
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.service.khdmaa.R
import com.service.khdmaa.databinding.ActivityLoginBinding
import com.service.khdmaa.ui.auth.signup.SignUpActivity
import com.service.khdmaa.ui.base.BaseActivity
import com.service.khdmaa.ui.main.home.MainActivity
import com.service.khdmaa.utiles.Utiles
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_login.view.*
import org.json.JSONException
import org.json.JSONObject
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*


@AndroidEntryPoint
class LoginActivity :BaseActivity<LoginViewModel,ActivityLoginBinding>() {
    lateinit var callbackManager: CallbackManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utiles.setLocalization(this, lang)
        /////
      //  callbackManager = CallbackManager.Factory.create()
        try {
            val info = packageManager.getPackageInfo(
                "com.service.khdmaa",
                PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Utiles.log_D("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {

        } catch (e: NoSuchAlgorithmException) {

        }


        val loginButton = findViewById<LoginButton>(R.id.login_button)
        callbackManager = CallbackManager.Factory.create()
//        loginButton.setOnClickListener(View.OnClickListener {
//            // Login
//            callbackManager = CallbackManager.Factory.create()
//            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"))
//            LoginManager.getInstance().registerCallback(callbackManager,
//                object : FacebookCallback<LoginResult> {
//                    override fun onSuccess(loginResult: LoginResult) {
//                        Utiles.log_D("MainActivity", "Facebook token: " + loginResult.accessToken.token)
//                        Utiles.log_D("MainActivity", "Facebook Permissions: " + loginResult.accessToken.permissions.)
//
//                        startActivity(Intent(applicationContext,SignUpActivity::class.java))
//                    }
//
//                    override fun onCancel() {
//                        Utiles.log_D("MainActivity", "Facebook onCancel.")
//
//                    }
//
//                    override fun onError(error: FacebookException) {
//                        Utiles.log_D("MainActivity", "Facebook onError.")
//                        error.printStackTrace()
//
//                    }
//                })
//        })


        loginButton.setReadPermissions(listOf("public_profile", "email"))
        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
            override fun onSuccess(loginResult: LoginResult?) {

                Utiles.log_D("Permissions", loginResult!!.accessToken.permissions)
                Utiles.log_D("FB_ID", loginResult!!.accessToken.userId)

                val request = GraphRequest.newMeRequest(
                    loginResult.accessToken,
                    object : GraphRequest.GraphJSONObjectCallback {
                        override fun onCompleted(
                            obj: JSONObject?,
                            response: GraphResponse?
                        ) {

                            try {
                                // Save user email to variable
                                //email = obj!!.getString("email")
                                //firstName = obj.getString("first_name")
                               // lastName = obj.getString("last_name")
                              //  handleFacebookAccessToken(token)
                                val intent=Intent(applicationContext,SignUpActivity::class.java)
                                intent.putExtra("FirstName",obj!!.getString("first_name"))
                                intent.putExtra("LastName",obj!!.getString("last_name"))
                                intent.putExtra("FbId",obj!!.getString("id"))
                                intent.putExtra("email",obj!!.getString("email"))


                                startActivity(intent)
                                Utiles.log_D("LoginActivity",obj.toString())
                            }
                            catch (e: JSONException) {
                                e.printStackTrace()

                                                 }
                        }
                    })

                val parameters = Bundle()
                parameters.putString("fields", "email,first_name,last_name")
                request.parameters = parameters
                request.executeAsync()


                // Utiles.log_D("cncncnncncncnc", loginResult!!.getAccessToken().permissions)
                // Get User's Info
            }

            override fun onCancel() {
                Toast.makeText(this@LoginActivity, "Login Cancelled", Toast.LENGTH_SHORT)
            }

            override fun onError(exception: FacebookException) {
                Toast.makeText(this@LoginActivity, exception.message, Toast.LENGTH_LONG)
                exception.printStackTrace()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode, resultCode, data)
    }
    override fun showLoader() {
         binding.loader.bar.visibility = View.VISIBLE
    }

    override fun hideLoader() {
        binding.loader.bar.visibility = View.GONE
    }

}