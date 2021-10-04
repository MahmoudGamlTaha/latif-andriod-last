package com.service.khdmaa.ui.main.services

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.service.khdmaa.R
import com.service.khdmaa.data.models.CategoryModel
import com.service.khdmaa.databinding.ClinicItemBinding
import com.service.khdmaa.databinding.ServicesItemBinding
import com.service.khdmaa.ui.main.clinic.ClinicAdapter
import com.service.khdmaa.ui.main.pets.PetsAdapter

class ServiceAdapter : RecyclerView.Adapter<ServiceAdapter.MyViewHolder>() {
    var list: List<CategoryModel> = mutableListOf()
        set(value) {
            field=value
            notifyDataSetChanged()
        }
    var action: PetsAdapter.CategoryActions? = null
    class MyViewHolder(val binding: ServicesItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ServicesItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val category = list.get(position).category
        holder.binding.title.setText(category.name)

        if (!category.icon.isNullOrEmpty()) {
            var image = category.icon

            Glide.with(holder.itemView.context).load(image)
                .error(R.drawable.ic_image)
                .placeholder(R.drawable.ic_image).into(holder.binding.image)
        }else holder.binding.image.setImageResource(R.drawable.ic_image)

        holder.itemView.setOnClickListener {
            action?.selectedCategory(category.id?:-1)
        }
    }

    override fun getItemCount(): Int = list.size
}