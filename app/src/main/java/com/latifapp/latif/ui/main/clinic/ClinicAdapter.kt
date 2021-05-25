package com.latifapp.latif.ui.main.clinic

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.postsapplication.network.NetworkClient
import com.latifapp.latif.R
import com.latifapp.latif.data.models.CategoryModel
import com.latifapp.latif.databinding.ClinicItemBinding
import com.latifapp.latif.ui.main.pets.PetsAdapter


class ClinicAdapter : RecyclerView.Adapter<ClinicAdapter.MyViewHolder>() {

    var list: List<CategoryModel> = mutableListOf()
        set(value) {
            field=value
            notifyDataSetChanged()
        }
    var action: PetsAdapter.CategoryActions? = null
    class MyViewHolder(val binding: ClinicItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder(
            ClinicItemBinding.inflate(
            LayoutInflater.from(parent.getContext()),
            parent, false
        ))
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

    override fun getItemCount(): Int =list.size

    data class Model(val title:Int,val image:Int)
}