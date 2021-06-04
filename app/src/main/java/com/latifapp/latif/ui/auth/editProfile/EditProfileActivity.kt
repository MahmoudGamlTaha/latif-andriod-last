package com.latifapp.latif.ui.auth.editProfile

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.latifapp.latif.R
import com.latifapp.latif.data.models.UserModel
import com.latifapp.latif.databinding.ActivityEditProfileBinding
import com.latifapp.latif.ui.auth.login.LoginViewModel
import com.latifapp.latif.ui.base.BaseActivity
import com.latifapp.latif.utiles.Permissions
import com.latifapp.latif.utiles.Utiles
import com.latifapp.latif.utiles.getRealPathFromGallery
import com.nguyenhoanglam.imagepicker.model.Config
import com.nguyenhoanglam.imagepicker.model.Image
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_profile.*
import java.io.File
import java.util.ArrayList

@AndroidEntryPoint
class EditProfileActivity : BaseActivity<EditProfileViewModel, ActivityEditProfileBinding>() {

    private lateinit var items: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        items = arrayOf<String>(
            getString(R.string.camera),
            getString(R.string.gallery),
            getString(R.string.cancel_)
        )
        binding.backBtn.setOnClickListener {
            onBackPressed()
        }

        setUserInfo(intent.extras?.get("model") as UserModel?)
        binding.profilePic.setOnClickListener {
            choose()
        }
        binding.editBtn.setOnClickListener({
            val name = binding.nameEx.text.toString()
            val email = binding.emailEx.text.toString()
            val phone = binding.phoneEx.text.toString()
            val country = binding.countryEx.text.toString()
            val city = binding.cityEx.text.toString()
            val address = binding.addressEx.text.toString()
            val govs = binding.governorateEx.text.toString()
            if (viewModel.validate(name, email, "", phone, country, address, city, govs, false))
                viewModel.edit().observe(this, Observer {
                    if (it != null) {
                        intent.putExtra("model", it)
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    }
                })
        })
        viewModel.validateLiveData.observe(this, Observer {
            if (it != null)
                when (it) {
                    LoginViewModel.SignUpFiled.email_ -> binding.emailEx.setError(getString(R.string.check_email))
                    LoginViewModel.SignUpFiled.name_ -> binding.nameEx.setError(getString(R.string.valid_name))
                    LoginViewModel.SignUpFiled.phone_ -> binding.phoneEx.setError(getString(R.string.valid_phone))
                    LoginViewModel.SignUpFiled.address_ -> binding.addressEx.setError(getString(R.string.valid_address))
                    LoginViewModel.SignUpFiled.city_ -> binding.cityEx.setError(getString(R.string.valid_city))
                    LoginViewModel.SignUpFiled.country_ -> binding.countryEx.setError(getString(R.string.valid_country))
                    LoginViewModel.SignUpFiled.governorate_ -> binding.governorateEx.setError(
                        getString(R.string.valid_govs)
                    )
                }
        })
    }


    private fun setUserInfo(it: UserModel?) {
        it?.apply {
            binding.addressEx.setText(address)
            binding.nameEx.setText(firstName + " " + lastName)
            binding.countryEx.setText(country)
            binding.emailEx.setText(email)
            binding.phoneEx.setText(phone)
            binding.cityEx.setText(city)
            binding.governorateEx.setText(state)

         setImageProfile(avatar)
        }

    }

    private fun setImageProfile(avatar: String?) {
        if (!avatar.isNullOrEmpty())
            Glide.with(this@EditProfileActivity)
                .load(avatar)
                .placeholder(R.drawable.ic_profile)
                .error(R.drawable.ic_profile)
                .into(binding.profilePic)
    }

    override fun setBindingView(inflater: LayoutInflater): ActivityEditProfileBinding {
        return ActivityEditProfileBinding.inflate(inflater)
    }

    override fun showLoader() {
        binding.loader.bar.visibility = View.VISIBLE
    }

    override fun hideLoader() {
        binding.loader.bar.visibility = View.GONE
    }

    private fun choose() {
        val builder = AlertDialog.Builder(this)
        builder.setItems(items, { dialog, which ->
            // Get the dialog selected item
            val selected = items[which]
            when (selected) {
                getString(R.string.camera) -> checkCamera()
                getString(R.string.gallery) -> checkGallery(true)
                getString(R.string.cancel_) -> dialog.dismiss()
            }

        })

        builder.create().show()
    }

    private fun checkGallery(isMultiple: Boolean) {
        if (!Permissions.checkCameraPermissions(this)) {
            Permissions.showPermissionsDialog(
                this,
                "External Storage  Permission Is Needed",
                Permissions.cameraManifestPermissionsList,
                Permissions.galleryRequest
            )
        } else {
            selectFromGallery(Permissions.galleryRequest, isMultiple)
        }
    }

    private fun selectFromGallery(galleryRequest: Int, isMultiple: Boolean) {
        ImagePicker.with(this) //  Initialize ImagePicker with activity or fragment context
            .setToolbarColor("#212121") //  Toolbar color
            .setStatusBarColor("#000000") //  StatusBar color (works with SDK >= 21  )
            .setToolbarTextColor("#FFFFFF") //  Toolbar text color (Title and Done button)
            .setToolbarIconColor("#FFFFFF") //  Toolbar icon color (Back and Camera button)
            .setProgressBarColor("#4CAF50") //  ProgressBar color
            .setBackgroundColor("#212121") //  Background color
            .setCameraOnly(false) //  Camera mode
            .setMultipleMode(isMultiple) //  Select multiple images or single image
            .setFolderMode(true) //  Folder mode
            .setShowCamera(false) //  Show camera button
            .setDoneTitle("Done") //  Done button title
            .setLimitMessage("You have reached selection limit") // Selection limit message
            .setAlwaysShowDoneButton(true) //  Set always show done button in multiple mode
            .setRequestCode(galleryRequest) //  Set request code, default Config.RC_PICK_IMAGES
            .setKeepScreenOn(true)
            .setSavePath("latif")
            .setMaxSize(4)
            .start()
    }


    private fun checkCamera() {
        if (!Permissions.checkCameraPermissions(this))
            Permissions.showPermissionsDialog(
                this,
                "Camera And Gallery Permission Is Needed",
                Permissions.cameraManifestPermissionsList,
                Permissions.galleryRequest
            )
        else takePhoto()
    }

    private fun takePhoto() {
        ImagePicker.with(this) //  Initialize ImagePicker with activity or fragment context
            .setCameraOnly(true)
            .setMultipleMode(false) //  Select multiple images or single image
            .setFolderMode(true) //  Folder mode
            .setShowCamera(false)
            .setAlwaysShowDoneButton(true) //  Set always show done button in multiple mode
            .setRequestCode(Permissions.galleryRequest)
            .setKeepScreenOn(true)
            .setSavePath("latif")
            .setMaxSize(1)
            .start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == Permissions.galleryRequest) {
                try {
                    val imagesList: ArrayList<Image> =
                        data!!.getParcelableArrayListExtra(Config.EXTRA_IMAGES)!!
                    val list = mutableListOf<String>()
                    for (model in imagesList) {
                        val uri = Uri.fromFile(File(model.path))
                        val path: String = uri.getRealPathFromGallery(this)
                        Utiles.log_D("dndnddd,dkkdkd2", path)
                        list.add(path)
                    }
                    for (image in list)
                        viewModel.uploadImage(image).observe(this, Observer {
                            if (!it.isNullOrEmpty() && !it.equals("-1")) {
                                setImageProfile(it)
                            }
                        })


                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

}