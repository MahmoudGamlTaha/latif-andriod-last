package com.service.khdmaa.ui.main.blogs.createBlog

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.service.khdmaa.R
import com.service.khdmaa.databinding.ActivityCreateBlogBinding
import com.service.khdmaa.databinding.CustomSpinnerLayoutBinding
import com.service.khdmaa.ui.base.BaseActivity
import com.service.khdmaa.ui.sell.adapters.ImagesAdapter
import com.service.khdmaa.ui.sell.views.CustomImagesList
import com.service.khdmaa.ui.sell.views.CustomParentView
import com.service.khdmaa.utiles.*
import com.nguyenhoanglam.imagepicker.model.Config
import com.nguyenhoanglam.imagepicker.model.Image
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.add_images_list.*
import kotlinx.coroutines.flow.collect
import java.io.File
import java.util.*

@AndroidEntryPoint
class CreateBlogActivity : BaseActivity<CreateBlogViewModel, ActivityCreateBlogBinding>() {
    private val adapter = ImagesAdapter()
    val listOfImages = mutableListOf<String>()
    var blogCategoryID: Int? = null
    private var items = arrayOf<String>()

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(MyContextWrapper.wrap(newBase, Utiles.LANGUAGE))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utiles.setLocalization(this, lang)
        binding.backBtn.setOnClickListener { onBackPressed() }
        items = arrayOf<String>(
            getString(R.string.camera),
            getString(R.string.gallery),
            getString(R.string.cancel_)
        )
        binding.submitBtn.setOnClickListener {
            val title = binding.titleEx.text.toString()
            val description = binding.descriptionEx.text.toString()
            binding.titleEx.setError(null)
            binding.descriptionEx.setError(null)
            if (title.isNullOrEmpty() && title.length < 3) {
                binding.titleEx.setError(getString(R.string.required))
            } else if (description.isNullOrEmpty() && title.length < 5) {
                binding.descriptionEx.setError(getString(R.string.required))
            } else
                lifecycleScope.launchWhenStarted {
                    viewModel.createBlog(
                        blogCategoryID, listOfImages,
                        title, description
                    ).collect { success ->
                        if (success) {
                            Toast.makeText(
                                this@CreateBlogActivity, R.string.blogIsAdded,
                                Toast.LENGTH_SHORT
                            ).show()
                            onBackPressed()
                        }
                    }
                }
        }
        createSpinner()
        createImagesList()
    }

    fun createSpinner() {
        lifecycleScope.launchWhenStarted {
            viewModel.getBlogsCategoryList().collect {
                val list: MutableList<String> = it.map { "${if (isEnglish) it.name else it.nameAr}" } as MutableList
                list.add(getString(R.string.choose))
                val arrayAdapter = this@CreateBlogActivity?.let {
                    HintAdapter(
                        it, android.R.layout.simple_list_item_1, list
                    )
                }

                binding.spinner.apply {
                    root.visibility = VISIBLE
                    label.text = getString(R.string.categories)
                    adsTypeSpinner.setAdapter(arrayAdapter)
                    adsTypeSpinner.setSelection(list.size-1)
                    adsTypeSpinner.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                if (position<it.size)
                                blogCategoryID = it.get(position).id
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {

                            }
                        }

                }
            }
        }
    }

    private fun createImagesList() {
        var header = getString(R.string.addphoto)
        val view = CustomImagesList(this, header!!, adapter, object :
            CustomParentView.ViewAction<View> {
            override fun getActionId(btn: View) {
                choose()
            }
        })


        binding.imagesListContainer.addView(view.getView())

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
            .setMaxSize(6)
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
                                adapter.list.add(it)
                                adapter.notifyDataSetChanged()
                                listOfImages.add(it)
                            }
                        })


                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun setBindingView(inflater: LayoutInflater): ActivityCreateBlogBinding {
        return ActivityCreateBlogBinding.inflate(inflater)
    }


    override fun showLoader() {
        binding.loader.bar.visibility = View.VISIBLE
    }

    override fun hideLoader() {

        binding.loader.bar.visibility = View.GONE
    }
}