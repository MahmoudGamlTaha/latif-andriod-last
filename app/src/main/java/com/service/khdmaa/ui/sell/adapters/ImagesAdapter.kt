package com.service.khdmaa.ui.sell.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.service.khdmaa.R
import com.service.khdmaa.databinding.PetImageItemBinding
import com.service.khdmaa.utiles.Utiles
import java.io.File

class ImagesAdapter : RecyclerView.Adapter<ImagesAdapter.MyViewHolder>() {
    val list = mutableListOf<String>()

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

        Utiles.log_D("ssjsjsjsjjs", list.get(position)+" "+list.size)
        Glide.with(holder.itemView.context).load(list.get(position))
            .placeholder(R.drawable.placeholder).error(R.drawable.placeholder)
            .into(holder.binding.image)

    }

    override fun getItemCount(): Int = list.size
}