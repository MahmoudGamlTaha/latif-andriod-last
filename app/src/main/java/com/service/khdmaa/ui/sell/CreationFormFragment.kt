package com.service.khdmaa.ui.sell

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.service.khdmaa.R
import com.service.khdmaa.data.models.AdsTypeModel
import com.service.khdmaa.data.models.RequireModel
import com.service.khdmaa.databinding.FragmentCreationFormBinding
import com.service.khdmaa.ui.base.BaseFragment
import com.service.khdmaa.ui.main.pets.PetsFragment
import com.service.khdmaa.ui.main.pets.PetsFragment.Companion.Latitude_
import com.service.khdmaa.ui.main.pets.PetsFragment.Companion.Longitude_
import com.service.khdmaa.ui.map.MapsActivity
import com.service.khdmaa.ui.sell.adapters.ImagesAdapter
import com.service.khdmaa.ui.sell.views.*
import com.service.khdmaa.utiles.HintAdapter
import com.service.khdmaa.utiles.Permissions
import com.service.khdmaa.utiles.Utiles
import com.service.khdmaa.utiles.getRealPathFromGallery
import com.nguyenhoanglam.imagepicker.model.Config
import com.nguyenhoanglam.imagepicker.model.Image
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_sell.*
import kotlinx.android.synthetic.main.custom_spinner_layout.*
import kotlinx.coroutines.flow.collect
import java.io.File
import java.util.ArrayList

@AndroidEntryPoint
class CreationFormFragment : BaseFragment<SellViewModel, FragmentCreationFormBinding>(),
    AdapterView.OnItemSelectedListener {
    private lateinit var typeList: List<AdsTypeModel>
    private var items = arrayOf<String>()
    private var lat = 0.0
    private var lng = 0.0

    private lateinit var liveData: MutableLiveData<String>
    private val hashMap: MutableMap<String, Any> = mutableMapOf()
    private val CurrentForm: MutableMap<String, Boolean?> = mutableMapOf()
    private val CurrentFormEng: MutableMap<String, String?> = mutableMapOf()
    private val CurrentFormRequiredCond: MutableMap<String, String?> = mutableMapOf()
    private var changeableValue: MutableLiveData<Pair<String, String>>? = null
    private val viewsForm: MutableMap<View, RequireModel> = mutableMapOf()
    private var CurrentSelectedPos = 0
    companion object {
        var Latitude_Filter = Latitude_

        var Longitude_Filter = Longitude_
    }

    override fun setBindingView(inflater: LayoutInflater): FragmentCreationFormBinding {
        return FragmentCreationFormBinding.inflate(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

         lat = PetsFragment.Latitude_
        lng = PetsFragment.Longitude_
        items = arrayOf<String>(
            getString(R.string.camera),
            getString(R.string.gallery),
            getString(R.string.cancel_)
        )

        if (viewModel.isSellAction)
            getAdsType()

        lifecycleScope.launchWhenStarted {
            viewModel.submitClick.collect {
                if (it)
                    viewModel.submitAdForm(
                        hashMap,
                        CurrentForm,
                        CurrentFormRequiredCond,
                        CurrentFormEng
                    )
            }
        }
    }

    private fun getAdsType() {
        lifecycleScope.launchWhenStarted {
            viewModel.getAdsTypeList().collect {
                if (!it.isNullOrEmpty()) {
                    typeList = it
                    setAdsTypeSpinnerData()
                }
            }
        }
    }


    private fun setAdsTypeSpinnerData() {
        val list_ = typeList.map { if (isEnglish) it.name else it.nameAr } as MutableList
        list_.add(getString(R.string.choose))
        val arrayAdapter = requireActivity()?.let {
            HintAdapter(
                it, android.R.layout.simple_list_item_1, list_
            )
        }

        binding.spinner.apply {
            adsTypeSpinner.setAdapter(arrayAdapter)
            adsTypeSpinner.onItemSelectedListener = this@CreationFormFragment
            label.text = getString(R.string.ads_types)
            root.visibility = View.VISIBLE
            adsTypeSpinner.setSelection(list_.size - 1)
        }

    }

    private fun setFormViews(form: List<RequireModel>) {
        CurrentForm.clear()
        CurrentFormEng.clear()
        CurrentFormRequiredCond.clear()
        for (model_ in form) {
            var view_: View? = null
            when (model_.type?.toLowerCase()) {
                "boolean" -> view_ = createSwitch(model_)
                "file" -> view_ = createImage(model_)
                "files" -> view_ = createImagesList(model_)
                "select" -> view_ = createCheckBoxGroup(model_)
                "dropdown" -> view_ = createSpinner(model_)?.view_
                "radiobutton" -> view_ = createRadioButtonGroup(model_)
                "map" -> view_ = createMapBtn(model_)
                "url_option" -> view_ = getUrlInfo(model_, true)?.view_
                "url_option_val" -> {
                    view_ = getChangeUrl(model_)
                }
                "text" -> view_ = createEditText(model_, true)
                else -> view_ = createEditText(model_)
            }

            setCondtions(model_)
            Utiles.log_D("mcmcmmcmcmcmmccm", CurrentFormRequiredCond)
            if (view_ != null) {
                if (!model_.show.isNullOrEmpty())
                    viewsForm.put(view_, model_)
            }
        }

    }

    private fun setCondtions(model_: RequireModel) {
        if (model_?.required == true) {
            CurrentForm[model_.name.toString()] = model_.required
            CurrentFormEng[model_.name.toString()] = model_.label
            CurrentFormRequiredCond[model_.name.toString()] = model_.requiredcond
            Utiles.log_D("cncnncncncncn11545454s54454554", model_.name)
            Utiles.log_D("cncnncncncncn11545454s54454554", CurrentFormRequiredCond)
        }
    }

    private fun removeCondtions(model_: RequireModel) {
        CurrentForm.remove(model_.name.toString())
        CurrentFormEng.remove(model_.name.toString())
        CurrentFormRequiredCond.remove(model_.name.toString())
        Utiles.log_D("cncnncncncncn11545454s54454554", model_.name)
        Utiles.log_D("cncnncncncncn11545454s54454554", CurrentFormRequiredCond)

    }

    fun checkShowAndHideViews(name: String) {
        for (views_form in viewsForm) {
            val view = views_form.key // view
            val values = views_form.value // required model
            var valid = true

            if (name.equals(values.name)) // cause type cant control show or hide of it self
                return
            Utiles.log_D("cncnncncncncn5512", name + "      " + values.name)

            for (value in values.show!!) {
                if (hashMap.containsKey(value.key)) {
                    // not in
                    if (hashMap[value.key] !in value.value) {
                        valid = false
                    }
                }
            }
            if (valid) {
                view.visibility = View.VISIBLE
                if (view.tag != null) {
                    setHashMapValues("${values?.name}", "${view?.tag}")
                    view.tag = null
                }
            } else {
                view.visibility = View.GONE
                view.tag = hashMap.get("${values?.name}")
                setHashMapValues("${values?.name}", "")
            }
        }
    }

    private fun getUrlInfo(model_: RequireModel, requestApi: Boolean): CustomSpinner {

        val requireModel = RequireModel(
            type = "dropDown",
            required = model_.required,
            name = model_.name,
            label = model_.label,
            label_ar = model_.label_ar,
            options = listOf()
        )
        var curr = createSpinner(requireModel)
        if (requestApi)
            getDataUrl(curr, model_)
        return curr
    }

    private fun getDataUrl(curr: CustomSpinner, model_: RequireModel): View? {
        viewModel.getUrlInfo(model_).observe(viewLifecycleOwner, Observer {
            if (it != null) {
                if (it.options?.isEmpty() == true) {
                    curr.view_?.visibility = View.GONE
                    removeCondtions(model_)
                } else {
                    setCondtions(model_)
                    curr.list_ = it.options!!
                    val list: MutableList<String> =
                        it.options!!.map { "${it.label}" } as MutableList<String>
                    list.add(getString(R.string.choose))
                    curr.arrayAdapter = HintAdapter(
                        context, android.R.layout.simple_list_item_1, list
                    )
                    curr.spinnerView.setAdapter(curr.arrayAdapter)
                    curr.spinnerView.setSelection(list.size - 1)
                    curr.view_?.visibility = View.VISIBLE
                }
            }

        })
        return curr.view_
    }

    private fun getChangeUrl(model_: RequireModel): View? {
        if (changeableValue == null)
            changeableValue = MutableLiveData(null)
        val spinner = getUrlInfo(model_, false)
        spinner.view_?.visibility = View.GONE // init values is null
        val url = model_.url
        val modelCopy = model_
        changeableValue?.observe(requireActivity(), Observer {

            if (it != null) {
                if (it.first.equals(model_.value)) {
                    setHashMapValues("${model_?.name}", "")
                    modelCopy.url = url + it.second
                    getDataUrl(spinner, modelCopy)
                    Utiles.log_D("dndndndndndn2255", it)
                    changeableValue?.value = null // reset
                }
            }

        })
        return spinner.view_
    }

    private fun createCheckBoxGroup(model: RequireModel): View? {
        var header = model.label
        if (!isEnglish)
            header = model.label_ar
        val text = CustomCheckBoxGroup(requireContext(), header!!, model.options!!, object :
            CustomParentView.ViewAction<String> {
            override fun getActionId(text: String) {
                setHashMapValues("${model.name}", "$text")
            }
        }
        )
        binding.container.addView(text.getView())
        return text.view_
    }

    private fun createRadioButtonGroup(model: RequireModel): View? {
        var header = model.label
        if (!isEnglish)
            header = model.label_ar
        val text = CustomRadioGroup(requireActivity(), header!!, model.options!!, object :
            CustomParentView.ViewAction<String> {
            override fun getActionId(text: String) {
                setHashMapValues("${model.name}", "$text")
            }
        }
        )
        binding.container.addView(text.getView())
        return text.view_
    }

    private fun createMapBtn(model: RequireModel): View? {
        binding.mapContainer.visibility = View.VISIBLE
        if (viewModel.isSellAction) {
            setHashMapValues("latitude", "$lat")
            setHashMapValues("longitude", "$lng")
        }
        binding.mapBtn.setOnClickListener {
            // intent to map
            if (!Permissions.checkLocationPermissions(requireActivity())) {
                Permissions.showPermissionsDialog(
                    requireActivity(),
                    getString(R.string.accessLocationMsg),
                    Permissions.locationManifestPermissionsList,
                    0
                )
            } else {

               this.startActivityForResult(
                    Intent(requireActivity(), MapsActivity::class.java),
                    Permissions.MapRequest
                )
            }
        }
        return binding.mapBtn
    }

    private fun createImage(model: RequireModel): View? {
        var header = model.label
        if (!isEnglish)
            header = model.label_ar
        val view = CustomImage(requireActivity(), header!!, object :
            CustomParentView.ViewAction<ImageView> {
            override fun getActionId(imageView: ImageView) {
                liveData = MutableLiveData<String>()
                liveData.observe(requireActivity(), Observer {
                    setHashMapValues("${model.name}", it)
                    Glide.with(requireActivity()).load(it).into(imageView)

                })
                choose(false)
            }
        })
        binding.container.addView(view.getView())
        return view.view_
    }

    private fun createImagesList(model: RequireModel): View? {
        val adapter = ImagesAdapter()
        val listOfImages = mutableListOf<String>()
        var header = model.label
        if (!isEnglish)
            header = model.label_ar
        val view = CustomImagesList(requireActivity(), header!!, adapter, object :
            CustomParentView.ViewAction<View> {
            override fun getActionId(btn: View) {
                liveData = MutableLiveData<String>()
                liveData.observe(requireActivity(), Observer {
                    adapter.list.add(it)
                    adapter.notifyDataSetChanged()
                    listOfImages.add(it)
                    hashMap.put(model.name!!, listOfImages)
                })
                choose(true)
            }
        })


        binding.container.addView(view.getView())

        return view.view_
    }

    fun createEditText(model: RequireModel, isMultiLine: Boolean = false): View? {

        if (model.label.isNullOrEmpty()) return null
        var header = model.label
        if (!isEnglish)
            header = model.label_ar
        val text =
            CustomEditText(
                requireActivity(),
                header!!,
                model.type?.toLowerCase().equals("string"),
                isMultiLine,
                object :
                    CustomParentView.ViewAction<String> {
                    override fun getActionId(text: String) {
                        setHashMapValues("${model.name}", "$text")
                    }
                }
            )

        binding.container.addView(text.getView())
        return text.view_
    }

    fun createSwitch(model: RequireModel): View? {
        var header = model.label
        if (!isEnglish)
            header = model.label_ar
        if (viewModel.isSellAction)
            setHashMapValues("${model.name}", "false")
        val switch = CustomSwitch(requireActivity(), header!!, object :
            CustomParentView.ViewAction<Boolean> {
            override fun getActionId(isON: Boolean) {
                setHashMapValues("${model.name}", "$isON")
            }
        }
        )
        binding.container.addView(switch.getView())
        return switch.view_
    }

    fun createSpinner(model: RequireModel): CustomSpinner {
        var header = model.label
        if (!isEnglish)
            header = model.label_ar
        val text = CustomSpinner(requireActivity(), header!!, model.options!!, object :
            CustomParentView.ViewAction<String> {
            override fun getActionId(text: String) {
                setHashMapValues("${model.name}", "$text")
            }
        },
            isEnglish
        )
        Utiles.log_D("smsmmsmsmsmsms", "$text")
        binding.container.addView(text.getView())
        return text
    }


    private fun choose(isMultiple: Boolean) {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setItems(items, { dialog, which ->
            // Get the dialog selected item
            val selected = items[which]
            when (selected) {
                getString(R.string.camera) -> checkCamera()
                getString(R.string.gallery) -> checkGallery(isMultiple)
                getString(R.string.cancel_) -> dialog.dismiss()
            }

        })

        builder.create().show()
    }

    private fun checkGallery(isMultiple: Boolean) {
        if (!Permissions.checkCameraPermissions(requireActivity())) {
            Permissions.showPermissionsDialog(
                requireActivity(),
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
        if (!Permissions.checkCameraPermissions(requireActivity()))
            Permissions.showPermissionsDialog(
                requireActivity(),
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
                        val path: String = uri.getRealPathFromGallery(requireActivity())
                        Utiles.log_D("dndnddd,dkkdkd2", path)
                        list.add(path)
                    }
                    for (image in list)
                        viewModel.uploadImage(image,typeList.get(this.CurrentSelectedPos).code!!).observe(requireActivity(), Observer {
                            if (!it.isNullOrEmpty() && !it.equals("-1"))
                                liveData.postValue(it)
                        })


                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else if (requestCode == Permissions.MapRequest) {
                lat = data!!.extras!!.getDouble("lat")
                lng = data!!.extras!!.getDouble("lng")
                Latitude_Filter = lat
                Longitude_Filter = lng
                setHashMapValues("latitude", "$lat")
                setHashMapValues("longitude", "$lng")
                val placename = data!!.extras!!.getString("placeName")
                binding.placeNme.text = placename
            }
        }
    }

    fun setHashMapValues(key: String, value: String) {
        if (value.isNullOrEmpty())
            hashMap.remove(key)
        else {
            hashMap.put("$key", value)
            changeableValue?.value = Pair(key, value)
            checkShowAndHideViews(key)
        }


    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (position < typeList.size) {
            CurrentSelectedPos=position;
            getForm(typeList.get(position).code)
            binding.container.removeAllViews()
            binding.mapContainer.visibility = View.GONE
            binding.placeNme.text = ""
            hashMap.clear()
            changeableValue = null
        }
    }

    fun getForm(type: String?) {
        lifecycleScope.launchWhenStarted {
            viewModel.getCreateForm(type!!).collect {
                if (!it.form.isNullOrEmpty()) {

                    setFormViews(it.form)
                }
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun showLoader() {
    }

    override fun hideLoader() {
    }
}