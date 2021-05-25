package com.latifapp.latif.ui.auth.signup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.latifapp.latif.R
import com.latifapp.latif.databinding.ActivitySignUpBinding
import com.latifapp.latif.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpActivity : BaseActivity<SignUpViewModel,ActivitySignUpBinding>() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

         Navigation.findNavController(this,
            R.id.fragment_container
        )
        binding.backBtn.setOnClickListener({
            onBackPressed()
        })

    }

    override fun setBindingView(inflater: LayoutInflater): ActivitySignUpBinding {
       return ActivitySignUpBinding.inflate(layoutInflater)
    }

    override fun showLoader() {
        binding.loader.bar.visibility = View.VISIBLE
     }

    override fun hideLoader() {
        binding.loader.bar.visibility = View.GONE
     }
}