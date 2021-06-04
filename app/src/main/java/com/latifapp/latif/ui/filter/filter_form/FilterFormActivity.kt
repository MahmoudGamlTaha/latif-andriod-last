package com.latifapp.latif.ui.filter.filter_form

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View

import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.latifapp.latif.R
import com.latifapp.latif.data.models.RequireModel
import com.latifapp.latif.databinding.ActivitySellBinding
import com.latifapp.latif.ui.base.BaseActivity
import com.latifapp.latif.ui.filter.filter_list.FilterActivity
import com.latifapp.latif.ui.map.MapsActivity
import com.latifapp.latif.ui.sell.views.*
import com.latifapp.latif.utiles.Permissions
import com.latifapp.latif.utiles.Utiles

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.io.Serializable

@AndroidEntryPoint
class FilterFormActivity : BaseActivity<FilterViewModel, ActivitySellBinding>() {

    private var filterHasMap: Boolean = false
    private var url: String? = ""
    private var lat = 0.0
    private var lng = 0.0
    private val hashMap: MutableMap<String, Any> = mutableMapOf()
    private var type:String? = ""
    private var isMap:Boolean? = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.backBtn.setOnClickListener { onBackPressed() }
        binding.title.setText(R.string.filter)
        binding.submitBtn.setText(R.string.filter)
        type=intent.extras?.getString("type")
        isMap=intent.extras?.getBoolean("isMap")
        getForm(type)

        binding.submitBtn.setOnClickListener {
            submitAdForm()
        }

    }

    private fun submitAdForm() {
        if (hashMap.isNullOrEmpty())
            toastMsg_Warning(getString(R.string.addFormValue), binding.root, this)
//        else if (lat == null || lat == 0.0) {
//            if (filterHasMap)
//                toastMsg_Warning(getString(R.string.plz_add_location), binding.root, this)
//            else {
//                lat = PetsFragment.Latitude_
//                lng = PetsFragment.Longitude_
//                setLocation()
//            }
//        }

        else {
            val intent = Intent(this, FilterActivity::class.java)
            intent.putExtra("url", url)
            intent.putExtra("type",type)
            intent.putExtra("isMaping",isMap)
            intent.putExtra("hashMap", hashMap as Serializable)
            startActivity(intent)
        }
    }


    private fun getForm(type: String?) {


        lifecycleScope.launchWhenStarted {
            viewModel.getCreateForm(type!!).collect {
                if (!it.form.isNullOrEmpty()) {
                    url = it.url
                    setFormViews(it.form)
                }
            }
        }
    }

    private fun setFormViews(form: List<RequireModel>) {
        Utiles.log_D("nffnnnfnfnf", "${lang != "en"}  $lang")
        for (model_ in form)
            when (model_.type?.toLowerCase()) {
                "boolean" -> createSwitch(model_)
                "select" -> createCheckBoxGroup(model_)
                "dropdown" -> createSpinner(model_)
                "radiobutton" -> createRadioButtonGroup(model_)
                "map" -> createMapBtn(model_)
                "url_option" -> getUrlInfo(model_)
                else -> createEditText(model_)


            }
    }

    private fun getUrlInfo(model_: RequireModel) {
       /* lifecycleScope.launchWhenStarted {
            viewModel.getUrlInfo(model_).observe(this@FilterFormActivity, Observer {
                if (it != null)
                    createSpinner(it)
            })


        }*/
        lifecycleScope.launchWhenResumed {
            val requireModel = RequireModel(
                type = "dropDown",
                required = model_.required,
                name = model_.name,
                label = model_.label,
                label_ar = model_.label_ar,
                options = listOf()
            )
            var curr= createSpinner(requireModel)
            viewModel.getUrlInfo(model_).observe(this@FilterFormActivity, Observer {

                if (it != null){
                    curr.list_=it.options!!
                    val list:List<String> =it.options!!.map { "${it.label}" }
                    curr.arrayAdapter!!.clear()
                    curr.arrayAdapter!!.addAll(list)
                    curr.arrayAdapter!!.notifyDataSetChanged()

                }

            })
        }

    }


    private fun createCheckBoxGroup(model: RequireModel) {
        var header = model.label
        if (lang != "en")
            header = model.label_ar
        val text = CustomCheckBoxGroup(this, header!!, model.options!!, object :
            CustomParentView.ViewAction<String> {
            override fun getActionId(text: String) {
                setHashMapValues("${model.name}", "$text")
            }
        }
        )
        binding.container.addView(text.getView())
    }

    private fun createRadioButtonGroup(model: RequireModel) {
        var header = model.label
        if (lang != "en")
            header = model.label_ar
        val text = CustomRadioGroup(this, header!!, model.options!!, object :
            CustomParentView.ViewAction<String> {
            override fun getActionId(text: String) {
                setHashMapValues("${model.name}", "$text")
            }
        }
        )
        binding.container.addView(text.getView())
    }

    private fun createMapBtn(model: RequireModel) {
        filterHasMap = true
        binding.mapContainer.visibility = View.VISIBLE
        binding.mapBtn.setOnClickListener {
            // intent to map
            if (!Permissions.checkLocationPermissions(this)) {
                Permissions.showPermissionsDialog(
                    this,
                    "Request Location permission Is Needed",
                    Permissions.locationManifestPermissionsList,
                    0
                )
            } else {

                startActivityForResult(
                    Intent(this, MapsActivity::class.java),
                    Permissions.MapRequest
                )
            }
        }
    }


    fun createEditText(model: RequireModel) {
        if (model.label.isNullOrEmpty()) return
        var header = model.label
        if (lang != "en")
            header = model.label_ar
        val text =
            CustomEditText(this, header, model.type?.toLowerCase().equals("string"),false, object :
                CustomParentView.ViewAction<String> {
                override fun getActionId(text: String) {
                    setHashMapValues("${model.name}", "$text")
                }
            }
            )
        binding.container.addView(text.getView())
    }

    fun createSwitch(model: RequireModel) {
        var header = model.label
        if (lang != "en")
            header = model.label_ar
        val switch = CustomSwitch(this, header!!, object :
            CustomParentView.ViewAction<Boolean> {
            override fun getActionId(isON: Boolean) {
                setHashMapValues("${model.name}", "$isON")
            }
        }
        )
        binding.container.addView(switch.getView())
    }

    fun createSpinner(model: RequireModel):CustomSpinner {
        var header = model.label
        if (lang != "en")
            header = model.label_ar
        val text = CustomSpinner(this, header!!, model.options!!, object :
            CustomParentView.ViewAction<String> {
            override fun getActionId(text: String) {
                setHashMapValues("${model.name}", "$text")
            }
        }
        )
        binding.container.addView(text.getView())
        return text

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Permissions.MapRequest) {
                lat = data!!.extras!!.getDouble("lat")
                lng = data!!.extras!!.getDouble("lng")
                setLocation()
                val placename = data!!.extras!!.getString("placeName")
                binding.placeNme.text = placename
            }
        }
    }

    private fun setLocation() {
        setHashMapValues("latitude", "$lat")
        setHashMapValues("longitude", "$lng")
    }

    override fun setBindingView(inflater: LayoutInflater): ActivitySellBinding {
        return ActivitySellBinding.inflate(inflater)
    }

    override fun showLoader() {
        binding.loader.bar.visibility = View.VISIBLE
    }

    override fun hideLoader() {

        binding.loader.bar.visibility = View.GONE
    }


    fun setHashMapValues(key: String, value: String) {
        if (value.isNullOrEmpty())
            hashMap.remove(key)
        else
            hashMap.put(key, value)


        Utiles.log_D("cncnncncncncn", hashMap)
    }


}