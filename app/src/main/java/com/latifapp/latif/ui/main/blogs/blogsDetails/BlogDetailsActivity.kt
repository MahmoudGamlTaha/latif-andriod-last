package com.latifapp.latif.ui.main.blogs.blogsDetails

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.latifapp.latif.R
import com.latifapp.latif.data.models.ImagesModel
import com.latifapp.latif.data.models.UserModel
import com.latifapp.latif.databinding.ActivityBlogDetailsBinding
import com.latifapp.latif.databinding.CallDialogBinding
import com.latifapp.latif.ui.zommingImage.ZoomingImageActivity
import com.latifapp.latif.ui.base.BaseActivity
import com.latifapp.latif.ui.details.PetImageAdapter
import com.latifapp.latif.utiles.MyContextWrapper
import com.latifapp.latif.utiles.Utiles

import dagger.hilt.android.AndroidEntryPoint
import java.io.Serializable

@AndroidEntryPoint
class BlogDetailsActivity : BaseActivity<BolgDetailsViewModel,ActivityBlogDetailsBinding>(),
    PetImageAdapter.Actions {

    private var phoneNum: String?=""
    private lateinit var callPopUp: PopupWindow

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(MyContextWrapper.wrap(newBase, Utiles.LANGUAGE))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utiles.setLocalization(this, lang)
        val id= intent.extras?.getInt("id")
        viewModel.getDetailsOfBlog(id).observe(this, Observer {
            setListOfImages(it.images,it.image,it.externalLink)

            binding.container.visibility = View.VISIBLE
            binding.dateTxt.text = it.createdDate
            binding.priceTxt.text = "${getString(R.string.categories)} : ${if (isEnglish) it.category else it.categoryAr}"
            binding.petName.text = it.title
            binding.descriptionTxt.text = it.description
            setSellerInfo(it.user, it.externalLink)

        })


        binding.callBtn.setOnClickListener {
            callDialogShow(it)
        }


        binding.backBtn.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setListOfImages(images: List<String>?, image: String?, externalLink: Boolean) {
        binding.recyclerView.apply {
            layoutManager =
                LinearLayoutManager(this@BlogDetailsActivity, LinearLayoutManager.HORIZONTAL, false)
            var adapter_ = BlogImagesAdapter(images)
            adapter_.action=this@BlogDetailsActivity
            adapter = adapter_
        }

    }
    private fun setSellerInfo(createdBy: UserModel?, external: Boolean) {
        binding.sellerNameTxt.text = "${createdBy?.firstName} ${createdBy?.lastName}"
        binding.joinedDateTxt.text = "${getString(R.string.joinedDate)} ${createdBy?.registrationDate}"
        binding.numAdsTxt.text = "${getString(R.string.myAds)}: ${createdBy?.adsCount}"
        phoneNum = createdBy?.phone
        val image =createdBy?.avatar
        if (!image.isNullOrEmpty()) {
            var imagePath=image

            Glide.with(this).load(imagePath)
                .error(R.drawable.ic_image)
                .placeholder(R.drawable.ic_image).into(binding.profilePic)
        }
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
                callPopUp.dismiss()
            }
        }
        if (callPopUp.isShowing)
            callPopUp.dismiss()
        else
            callPopUp.showAsDropDown(view)
    }

    override fun setBindingView(inflater: LayoutInflater): ActivityBlogDetailsBinding {
        return ActivityBlogDetailsBinding.inflate(inflater)
    }

    override fun showLoader() {
        binding.loader.bar.visibility= View.VISIBLE
    }

    override fun hideLoader() {
        binding.loader.bar.visibility= View.GONE
    }

    override fun onImageClick(images:List<String>?, position: Int) {
        val intent = Intent(this, ZoomingImageActivity::class.java)
        intent.putExtra("images", images as (Serializable))
        intent.putExtra("position", position)
        startActivity(intent)
    }
}