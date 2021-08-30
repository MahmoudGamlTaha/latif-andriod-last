package com.latifapp.latif.ui.main.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.latifapp.latif.R
import com.latifapp.latif.data.local.AppPrefsStorage
import com.latifapp.latif.data.models.UserModel
import com.latifapp.latif.databinding.ActivityMainBinding
import com.latifapp.latif.databinding.FragmentProfileBinding
import com.latifapp.latif.ui.auth.editProfile.EditProfileActivity
import com.latifapp.latif.ui.base.BaseActivity
import com.latifapp.latif.ui.fav.FavActivity
import com.latifapp.latif.ui.intrests.IntresetsActivity
import com.latifapp.latif.ui.main.chat.chatHistoryList.ChatHistoryListActivity
import com.latifapp.latif.ui.main.home.MainActivity
import com.latifapp.latif.ui.main.profile.dialog.LanguageDialogFragment
import com.latifapp.latif.ui.myAds.MyAdsActivity
import com.latifapp.latif.utiles.MyContextWrapper
import com.latifapp.latif.utiles.Utiles
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileActivity : BaseActivity<ProfileViewModel, FragmentProfileBinding>() {

    private val langDialog = LanguageDialogFragment()

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(MyContextWrapper.wrap(newBase, Utiles.LANGUAGE))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utiles.setLocalization(this, lang)
        viewModel.getUserInfo()
        viewModel.userInfo.observe(this, Observer {
            if (it!=null) {
                setUserInfo(it)
                binding.profileContainer.visibility = View.VISIBLE
            }
        })
        binding.backBtn.setOnClickListener {
            onBackPressed()
        }
        binding.myAds.setOnClickListener {
            startActivity(Intent(this, MyAdsActivity::class.java))
        }
        binding.chat.setOnClickListener {
            startActivity(Intent(this, ChatHistoryListActivity::class.java))
        }
        binding.intrests.setOnClickListener {
            startActivity(Intent(this, IntresetsActivity::class.java))
        }

        binding.favBtn.setOnClickListener {
            startActivity(Intent(this, FavActivity::class.java))
        }
        binding.logout.setOnClickListener {

            viewModel.logout().observe(this, Observer {
                if (it)onBackPressed()
            })
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
                        if (it) {
                            Utiles.setLocalization(this@ProfileActivity, language)
                            startActivity(Intent(this@ProfileActivity, MainActivity::class.java))
                            finishAffinity()
                        }
                    })

                }

            }

        }
    }

    private fun setUserInfo(it: UserModel?) {
        val model=it

        binding.addressValue.text = it?.address
        binding.nameValue.text = it?.firstName + " " + it?.lastName
        binding.birthdayValue.text = it?.registrationDate
        binding.genderValue.text = ""
        binding.emailValue.text = it?.email
        binding.phoneValue.text = it?.phone
        binding.addressValue.text = it?.address
        setImageProfile(it?.avatar)
        if (it!=null)
        binding.editBtn.visibility = View.VISIBLE

        binding.editBtn.setOnClickListener {
            var intent = Intent(this@ProfileActivity, EditProfileActivity::class.java)
            if (model != null)
                intent.putExtra("model", model)
            startActivityForResult(intent, 5)
        }
    }

    private fun setImageProfile(avatar: String?) {
        if (!avatar.isNullOrEmpty())
            Glide.with(this@ProfileActivity)
                .load(avatar)
                .placeholder(R.drawable.ic_profile)
                .error(R.drawable.ic_profile)
                .into(binding.profilePic)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 5 && resultCode == Activity.RESULT_OK) {
            val model: UserModel = data?.extras?.get("model") as UserModel
            setUserInfo(model)
        }
    }
}