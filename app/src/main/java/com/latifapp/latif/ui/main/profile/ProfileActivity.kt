package com.latifapp.latif.ui.main.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.latifapp.latif.R
import com.latifapp.latif.data.local.AppPrefsStorage
import com.latifapp.latif.databinding.ActivityMainBinding
import com.latifapp.latif.databinding.FragmentProfileBinding
import com.latifapp.latif.ui.base.BaseActivity
import com.latifapp.latif.ui.fav.FavActivity
import com.latifapp.latif.ui.main.home.MainActivity
import com.latifapp.latif.ui.main.profile.dialog.LanguageDialogFragment
import com.latifapp.latif.ui.myAds.MyAdsActivity
import com.latifapp.latif.utiles.Utiles
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileActivity : BaseActivity<ProfileViewModel, FragmentProfileBinding>() {


    private val langDialog = LanguageDialogFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.backBtn.setOnClickListener {
            onBackPressed()
        }
        binding.myAds.setOnClickListener {
            startActivity(Intent(this, MyAdsActivity::class.java))
        }
        binding.favBtn.setOnClickListener {
            startActivity(Intent(this, FavActivity::class.java))
        }
        binding.logout.setOnClickListener {
            viewModel.logout()
            onBackPressed()
        }
        binding.language.setOnClickListener {
            langDialog.lang = viewModel.lang
            langDialog.show(supportFragmentManager, "langDialog")
        }
        langDialog.action = object : LanguageDialogFragment.LanguageAction {
            override fun setLanguage(language: String) {
                langDialog.dismiss()
                if (!language.equals(lang)) {
                    viewModel.changeLanguage(language).observe(this@ProfileActivity, Observer {
                        if (it){
                            Utiles.setLocalization( this@ProfileActivity,language)
                            startActivity(Intent(this@ProfileActivity, MainActivity::class.java))
                            finishAffinity()
                        }
                    })

                }

            }

        }
    }

    override fun setBindingView(inflater: LayoutInflater): FragmentProfileBinding {
        return FragmentProfileBinding.inflate(layoutInflater)
    }

    override fun showLoader() {
        binding.loader.bar.visibility = View.VISIBLE
    }

    override fun hideLoader() {
        binding.loader.bar.visibility = View.GONE
    }

}