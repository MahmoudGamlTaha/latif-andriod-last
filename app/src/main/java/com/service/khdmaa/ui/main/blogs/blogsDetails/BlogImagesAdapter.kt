package com.service.khdmaa.ui.main.blogs.blogsDetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.service.khdmaa.R
import com.service.khdmaa.data.models.ImagesModel
import com.service.khdmaa.databinding.PetImageItemBinding
import com.service.khdmaa.ui.details.PetImageAdapter

class BlogImagesAdapter(val images: List<String>?) :
    RecyclerView.Adapter<BlogImagesAdapter.MyViewHolder>() {

    var action: PetImageAdapter.Actions?=null
    class MyViewHolder(val binding: PetImageItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            PetImageItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val image =images?.get(position)

            Glide.with(holder.itemView.context).load(image)
                .error(R.drawable.ic_image)
                .placeholder(R.drawable.ic_image).into(holder.binding.image)

        holder.binding.image.setOnClickListener {
            action?.onImageClick(images,position)
        }
    }

    override fun getItemCount(): Int {
        return if (images.isNullOrEmpty()) 0
        else images.size
    }
}