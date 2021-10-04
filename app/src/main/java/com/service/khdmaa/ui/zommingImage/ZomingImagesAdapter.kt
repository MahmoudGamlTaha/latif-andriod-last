package com.service.khdmaa.ui.zommingImage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.service.khdmaa.R
import com.service.khdmaa.data.models.ImagesModel
import com.service.khdmaa.databinding.ImageItemBinding
import com.service.khdmaa.databinding.PetImageItemBinding

class ZomingImagesAdapter(val images: List<String>?) :
    RecyclerView.Adapter<ZomingImagesAdapter.MyViewHolder>() {

     class MyViewHolder(val binding: ImageItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ImageItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val image =images?.get(position)
        if (!image.isNullOrEmpty()) {
            var imagePath=image
            Glide.with(holder.itemView.context).load(imagePath)
                .error(R.drawable.ic_image)
                .placeholder(R.drawable.ic_image).into(holder.binding.image)
        }else holder.binding.image.setImageResource(R.drawable.ic_image)
    }

    override fun getItemCount(): Int {
        return if (images.isNullOrEmpty()) 0
        else images.size
    }


}