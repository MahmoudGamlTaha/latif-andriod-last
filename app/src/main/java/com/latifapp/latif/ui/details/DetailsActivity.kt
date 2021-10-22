package com.latifapp.latif.ui.details

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.PopupWindow
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.latifapp.latif.R
import com.latifapp.latif.data.models.ExtraModel
import com.latifapp.latif.data.models.ImagesModel
import com.latifapp.latif.data.models.MsgNotification
import com.latifapp.latif.data.models.UserModel
import com.latifapp.latif.databinding.ActivityDetailsBinding
import com.latifapp.latif.databinding.CallDialogBinding
import com.latifapp.latif.databinding.TopOptionMenuBinding
import com.latifapp.latif.ui.zommingImage.ZoomingImageActivity
import com.latifapp.latif.ui.base.BaseActivity
import com.latifapp.latif.ui.details.dialog.ReportDialogFragment
import com.latifapp.latif.ui.filter.filter_list.FilterListViewModel
import com.latifapp.latif.ui.main.chat.chatPage.ChatPageActivity
import com.latifapp.latif.utiles.MyContextWrapper
import com.latifapp.latif.utiles.Utiles
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_report_dialog.*
import java.io.Serializable

@AndroidEntryPoint
class DetailsActivity() : BaseActivity<DetailsViewModel, ActivityDetailsBinding>(),
    OnMapReadyCallback, PetImageAdapter.Actions {
    override val viewModel by viewModels<DetailsViewModel>()
    private var adOwnerId: Int? = 0
    private var adOwnerName: String? = ""
    private var adOwnerPic: String? = ""
    private var reportDialog: ReportDialogFragment? = null

    private var adapter_: PetImageAdapter? = null
    private var mMap: GoogleMap? = null
    private var phoneNum: String? = ""
    private lateinit var topMenuPopUp: PopupWindow
    private lateinit var callPopUp: PopupWindow
    private var id: Int? = null

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(MyContextWrapper.wrap(newBase, Utiles.LANGUAGE))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utiles.setLocalization(this, lang)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.pippin)


        }
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        id = intent.extras?.getInt("ID")
        getDetails()


        binding.callBtn.setOnClickListener {
            callDialogShow(it)
        }

        binding.menuBtn.setOnClickListener {

            topMenuDialogShow(it)

        }
        binding.backBtn.setOnClickListener {
            onBackPressed()
        }

        viewModel.report_liveData.observe(this, Observer {
            if (it != null) {
                if (it.success ?: false)
                    toastMsg_Success("${it.msg}", binding.root, this)
                else toastMsg_Warning("${it.msg}", binding.root, this)
                reportDialog?.dismiss()
            }
        })
    }

    private fun getDetails() {
        viewModel.getAdDetails(id).observe(this, Observer {
            if (it != null) {
                setListOfImages(it.images, it.image, it.external_link)
                binding.container.visibility = View.VISIBLE
                binding.dateTxt.text = it.created_at
                binding.priceTxt.text = "${it.price} EGP"
                binding.petName.text = it.name
                binding.locationTxt.text = it.city
                binding.descriptionTxt.text = it.description
                moveToLocation(it.latitude, it.longitude)
                setSellerInfo(it.createdBy, it.external_link)
                if (!it.extra.isNullOrEmpty())
                    setExtraList(it.extra)
            }
        })
    }

    private fun moveToLocation(lat: Double, lag: Double) {
        mMap?.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    lat,
                    lag
                ), 16f
            )
        )
        binding.mabBtn.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(
                    "http://maps.google.com/maps?f=d&daddr=${lat},${lag}"
                )
            )
            startActivity(intent)
        }
    }

    private fun setExtraList(extra: List<ExtraModel>) {
        binding.extraList.apply {
            layoutManager = GridLayoutManager(this@DetailsActivity, 2)
            adapter = ExtraAdapter(extra, viewModel.lang.equals("en"))
        }
    }

    private fun setSellerInfo(createdBy: UserModel?, external: Boolean) {
        binding.sellerNameTxt.text = "${createdBy?.firstName} ${createdBy?.lastName}"
        binding.joinedDateTxt.text =
            "${getString(R.string.joinedDate)} ${createdBy?.registrationDate}"
        binding.numAdsTxt.text = "${getString(R.string.myAds)}: ${createdBy?.adsCount}"
        phoneNum = createdBy?.phone
        adOwnerId = createdBy?.id
        adOwnerName = createdBy?.firstName
        adOwnerPic = createdBy?.avatar
        val image = adOwnerPic
        if (!image.isNullOrEmpty()) {
            var imagePath = image

            Glide.with(this).load(imagePath)
                .error(R.drawable.ic_image)
                .placeholder(R.drawable.ic_image).into(binding.profilePic)
        }
    }

    private fun setListOfImages(images: List<ImagesModel>?, image: String?, externalLink: Boolean) {
        binding.recyclerView.apply {
            layoutManager =
                LinearLayoutManager(this@DetailsActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter_ = PetImageAdapter(images)
            adapter_?.action = this@DetailsActivity
            adapter = adapter_
        }

    }

    private fun topMenuDialogShow(view: View?) {
        if (!::topMenuPopUp.isInitialized) {
            val popupBinding = TopOptionMenuBinding.inflate(layoutInflater)
            topMenuPopUp = PopupWindow(
                popupBinding.root,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            topMenuPopUp.setFocusable(true)
            topMenuPopUp.setOutsideTouchable(true)
            popupBinding.parentBg.setOnTouchListener { v, event ->
                topMenuPopUp.dismiss()
                true
            }
            popupBinding.shareBtn.setOnClickListener {
                val url = "https://latifapp.com?adsID=$id"
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, url)
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
                topMenuPopUp.dismiss()
            }
            popupBinding.reportBtn.setOnClickListener {
                // show report dialog
                if (viewModel.token.isNullOrEmpty()) {
                    toastMsg_Warning(getString(R.string.loginFirst), binding.root, this)
                } else {
                    reportDialog = ReportDialogFragment()
                    reportDialog?.show(supportFragmentManager, "ReportDialog")

                }
                topMenuPopUp.dismiss()
            }

            popupBinding.favBtn.setOnClickListener {
                // show report dialog
                if (viewModel.token.isNullOrEmpty()) {
                    toastMsg_Warning(getString(R.string.loginFirst), binding.root, this)
                } else {
                    setFavAd()
                }
                topMenuPopUp.dismiss()
            }
        }
        if (topMenuPopUp.isShowing)
            topMenuPopUp.dismiss()
        else
            topMenuPopUp.showAtLocation(view, Gravity.END, 50, 50)
    }

    private fun setFavAd() {
        viewModel.favAd().observe(this, Observer {
            if (it != null) {
                if (it.success ?: false)
                    toastMsg_Success("${it.msg}", binding.root, this)
                else toastMsg_Warning("${it.msg}", binding.root, this)
            }
        })
    }


    private fun callDialogShow(view: View) {
        if (!::callPopUp.isInitialized) {
            val popupBinding = CallDialogBinding.inflate(layoutInflater)
            callPopUp = PopupWindow(
                popupBinding.root,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            callPopUp.setFocusable(true)
            callPopUp.setOutsideTouchable(true)
            popupBinding.smsBtn.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("sms:${Uri.parse(phoneNum)}"))
                startActivity(intent)
                callPopUp.dismiss()
            }
            popupBinding.callBtn.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${Uri.parse(phoneNum)}"))
                startActivity(intent)
                callPopUp.dismiss()
            }
            popupBinding.chatBtn.setOnClickListener {
                if (viewModel.token.isNullOrEmpty()) {
                    toastMsg_Warning(getString(R.string.loginFirst), binding.root, this)
                } else if (viewModel.userID.equals("${adOwnerId}")) {
                    toastMsg_Warning(getString(R.string.cannotChat), binding.root, this)
                } else {
                    intentToChat()
                }
                callPopUp.dismiss()
            }
        }
        if (callPopUp.isShowing)
            callPopUp.dismiss()
        else
            callPopUp.showAsDropDown(view)
    }

    private fun intentToChat() {
        viewModel.checkIfHaveRoom().observe(this, Observer {
            if ( it!= null) {
                val intent = Intent(this, ChatPageActivity::class.java)
                val msg = MsgNotification(
                    sender_id = "${adOwnerId}",
                    sender_name = adOwnerName,
                    sender_avater = adOwnerPic,
                    message = "",
                    chat_room = it,
                    prod_id = "$id",
                    prod_name = ""
                )
                intent.putExtra("model", msg)
                startActivity(intent)
            }
        })

    }

    override fun setBindingView(inflater: LayoutInflater): ActivityDetailsBinding {
        return ActivityDetailsBinding.inflate(inflater)
    }

    override fun showLoader() {
        binding.loader.bar.visibility = View.VISIBLE
    }

    override fun hideLoader() {
        binding.loader.bar.visibility = View.GONE
    }

    override fun onMapReady(mMap: GoogleMap?) {
        this.mMap = mMap;
        mMap?.getUiSettings()?.setScrollGesturesEnabled(false);
    }

    override fun onImageClick(images:List<String>?,position: Int) {
        val intent = Intent(this, ZoomingImageActivity::class.java)
        intent.putExtra("images", images as (Serializable))
        intent.putExtra("position", position)
        startActivity(intent)
    }


}