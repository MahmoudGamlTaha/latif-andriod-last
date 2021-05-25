package com.latifapp.latif.ui.main.pets

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.postsapplication.network.NetworkClient.BASE_URL
import com.latifapp.latif.R
import com.latifapp.latif.data.models.CategoryItemsModel
import com.latifapp.latif.data.models.CategoryModel
import com.latifapp.latif.databinding.PetItemLayoutBinding
import com.latifapp.latif.databinding.SelectedPetItemBinding

class PetsAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val SELECT_ITEM = 0
    private val UN_SELECT_ITEM = 1
    private var selectedPosition = -1
    var action: CategoryActions? = null
    val list = mutableListOf<CategoryModel>()

    public fun clear() {
        selectedPosition = -1
        list.clear()
        notifyDataSetChanged()
    }

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
        val category = list.get(position).category
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
        holder.binding.text.text = category.name
        holder.itemView.setOnClickListener {
            holder.binding.rootv.apply {
                selectedPosition = -1
                notifyDataSetChanged()
                action?.selectedCategory(-1)
            }
        }
        if (!category.iconSelect.isNullOrEmpty()) {
            var image = category.iconSelect

            Glide.with(holder.itemView.context).load(image)
                .error(R.drawable.ic_image)
                .placeholder(R.drawable.ic_image).into(holder.binding.image)
        } else holder.binding.image.setImageResource(R.drawable.ic_image)
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
                action?.selectedCategory(category.id?:-1)
            }

        }
        holder.binding.text.text = category.name
        if (!category.icon.isNullOrEmpty()) {
            var image = category.icon

            Glide.with(holder.itemView.context).load(image).error(R.drawable.ic_image)
                .placeholder(R.drawable.ic_image).into(holder.binding.image)
        } else holder.binding.image.setImageResource(R.drawable.ic_image)
    }

    override fun getItemCount(): Int = list.size

    interface CategoryActions {
        fun selectedCategory(id: Int)
    }
}