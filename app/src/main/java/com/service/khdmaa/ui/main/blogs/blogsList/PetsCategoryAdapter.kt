package com.service.khdmaa.ui.main.blogs.blogsList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.service.khdmaa.R
import com.service.khdmaa.data.models.CategoryItemsModel
import com.service.khdmaa.databinding.PetItemLayoutBinding
import com.service.khdmaa.databinding.SelectedPetItemBinding

class PetsCategoryAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val SELECT_ITEM = 0
    private val UN_SELECT_ITEM = 1
    private var selectedPosition = -1
    var action: CategoryActions? = null
    var isEnglish=true
    val list = mutableListOf<CategoryItemsModel>()

    class MyViewHolder constructor(val binding: PetItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    class SelectedMyViewHolder constructor(val binding: SelectedPetItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == UN_SELECT_ITEM)
            return MyViewHolder(
                PetItemLayoutBinding.inflate(
                    LayoutInflater.from(parent.getContext()),
                    parent, false
                )
            ) else return SelectedMyViewHolder(
            SelectedPetItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent, false
            )
        )
    }

    override fun getItemViewType(position: Int): Int {
        if (selectedPosition == position)
            return SELECT_ITEM
        else return UN_SELECT_ITEM
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val category = list.get(position)
        if (holder is MyViewHolder) {
            setViewsAsUnSelecte(holder, position, category)
        } else if (holder is SelectedMyViewHolder) {
            setViewsAsSelecte(holder, position, category)
        }
    }

    private fun setViewsAsSelecte(
        holder: SelectedMyViewHolder,
        position: Int,
        category: CategoryItemsModel
    ) {
          holder.binding.text.text = if (isEnglish)category.name else category.nameAr
        holder.itemView.setOnClickListener {
            holder.binding.rootv.apply {
                selectedPosition = -1
                notifyDataSetChanged()
                action?.selectedCategory(null)
            }
        }
        if (!category.iconSelect.isNullOrEmpty()) {
            var image=category.iconSelect

            Glide.with(holder.itemView.context).load(image)
                .error(R.drawable.ic_image)
                .placeholder(R.drawable.ic_image).into(holder.binding.image)
        }else holder.binding.image.setImageResource(R.drawable.ic_image)
    }

    private fun setViewsAsUnSelecte(
        holder: MyViewHolder,
        position: Int,
        category: CategoryItemsModel
    ) {
        holder.itemView.setOnClickListener {
            holder.binding.rootv.apply {
                selectedPosition = position
                notifyDataSetChanged()
                action?.selectedCategory(category.id!!)
            }

        }
        holder.binding.text.text = if (isEnglish)category.name else category.nameAr
        if (!category.icon.isNullOrEmpty()) {
            var image=category.icon

            Glide.with(holder.itemView.context).load(image).error(R.drawable.ic_image)
                .placeholder(R.drawable.ic_image).into(holder.binding.image)
        }else holder.binding.image.setImageResource(R.drawable.ic_image)
    }

    override fun getItemCount(): Int = list.size

    interface CategoryActions {
        fun selectedCategory(id: Int?)
    }
}