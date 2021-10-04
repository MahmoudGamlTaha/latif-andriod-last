package com.service.khdmaa.ui.auth.signup.fragments.interests

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.service.khdmaa.R
import com.service.khdmaa.data.models.CategoryModel
import com.service.khdmaa.databinding.InterestItemBinding
import com.service.khdmaa.utiles.Utiles

class InterestsAdapter() : RecyclerView.Adapter<InterestsAdapter.MyViewHolder>() {
    var lang: String = Utiles.LANGUAGE
    var list: MutableList<CategoryModel> = mutableListOf()
        set(value) {
            field.clear()
            field.addAll(value)
            notifyDataSetChanged()
        }
    var selectedList = mutableSetOf<Int?>()

    class MyViewHolder constructor(val binding: InterestItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var isSelected = false
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InterestsAdapter.MyViewHolder {
        return MyViewHolder(
            InterestItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: InterestsAdapter.MyViewHolder, position: Int) {
        val category = list.get(position).category
        holder.binding.textItem.setText(if (lang.equals("en")) category.name else category.nameAr)
        holder.binding.textItem.setOnClickListener {
            holder.isSelected = !holder.isSelected
            if (!holder.isSelected) {
                selectedList.remove(category.id)
            } else {
                selectedList.add(category.id)
            }
            notifyItemChanged(position)
        }
        if (category.id in selectedList) {
            holder.isSelected = true
        } else holder.isSelected = false

        if (!holder.isSelected) {

            holder.binding.textItem.apply {
                setBackgroundResource(R.drawable.unselect_bg)
                setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.sunset_orange))
            }
        } else {

            holder.binding.textItem.apply {
                setBackgroundResource(R.drawable.select_bg)
                setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.white))
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}