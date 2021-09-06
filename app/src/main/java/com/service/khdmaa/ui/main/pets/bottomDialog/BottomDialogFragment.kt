package com.service.khdmaa.ui.main.pets.bottomDialog

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.service.khdmaa.R
import com.service.khdmaa.data.models.AdsModel
import com.service.khdmaa.databinding.FragmentBottomDialogBinding
import com.service.khdmaa.ui.zommingImage.ZoomingImageActivity
import com.service.khdmaa.ui.details.DetailsActivity
import com.service.khdmaa.ui.details.PetImageAdapter
import kotlinx.android.synthetic.main.fragment_bottom_dialog.*
import java.io.Serializable


class BottomDialogFragment : BottomSheetDialogFragment(), PetImageAdapter.Actions {


    private lateinit var binding: FragmentBottomDialogBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
          binding=FragmentBottomDialogBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adsModel: AdsModel? =arguments?.getParcelable("model")
        binding.recyclerView.apply {
            layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            val adapter_=PetImageAdapter(adsModel?.images)
            adapter_.action=this@BottomDialogFragment
            adapter= adapter_
        }
        binding.title.text=adsModel?.name
        binding.dateTxt.text=adsModel?.created_at
        binding.description.text=adsModel?.short_description
        binding.priceTxt.text=adsModel?.price+" EGP"
        binding.root.setOnClickListener {
            val intent=Intent(context,DetailsActivity::class.java)
            intent.putExtra("ID",adsModel?.id)
            startActivity(intent)
        }
        val phone=adsModel?.createdBy?.phone
        if (phone.isNullOrEmpty())
            binding.callBtn.visibility=GONE

        binding.callBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${Uri.parse(phone)}"))
            startActivity(intent)
        }
     }

    override fun onImageClick(images:List<String>?, position: Int) {
        val intent = Intent(context, ZoomingImageActivity::class.java)
        intent.putExtra("images", images as (Serializable))
        intent.putExtra("position", position)
        startActivity(intent)
    }

}