package com.latifapp.latif.ui.main.blogs.blogsList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.latifapp.latif.R
import com.latifapp.latif.data.models.BlogsModel
import com.latifapp.latif.databinding.BlogItemBinding
import com.latifapp.latif.ui.main.items.PetsListAdapter
import javax.inject.Inject

class BlogsAdapter @Inject constructor() : RecyclerView.Adapter<BlogsAdapter.MyViewHolder>() {
    var list = mutableListOf<BlogsModel>()
        set(value) {
            field.addAll(value)
            notifyDataSetChanged()

        }

    var action: PetsListAdapter.Action? = null
    class MyViewHolder(val binding: BlogItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            BlogItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.binding.text.text = list.get(position).title
        holder.binding.authorName.text =
            "${list.get(position).user?.firstName} ${list.get(position).user?.lastName}"
        holder.binding.dateTxt.text = list.get(position).createdDate

        val img=list.get(position).image
        if (!img.isNullOrEmpty()) {
            var imagePath=img

            Glide.with(holder.itemView.context).load(imagePath)
                .error(R.drawable.ic_image)
                .placeholder(R.drawable.ic_image).into(holder.binding.image)
        }else holder.binding.image.setImageResource(R.drawable.ic_image)

        holder.itemView.setOnClickListener {
            action?.onAdClick(list.get(position).id)
        }
    }

    override fun getItemCount(): Int = list.size
}