package com.latifapp.latif.ui.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.latifapp.latif.R
import com.latifapp.latif.databinding.MenuItemBinding

class MenuAdapter(val action: MenuAction) : RecyclerView.Adapter<MenuAdapter.MyViewHolder>() {
    val list = listOf(
        R.string.pets, R.string.items,
        R.string.service, R.string.blogs,R.string.subscribe,
        R.string.profile
    )

    class MyViewHolder(val binding: MenuItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            MenuItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.text.setText(list.get(position))
        holder.itemView.setOnClickListener {
            var anEnum = MenuEnum.pets
            when (position) {
                0 -> anEnum = MenuEnum.pets
                1 -> anEnum = MenuEnum.items
                2 -> anEnum = MenuEnum.service
                3 -> anEnum = MenuEnum.blogs
                4 -> anEnum = MenuEnum.subscribe
                5 -> anEnum = MenuEnum.profile
            }
            action.menuClick(anEnum)
        }
    }

    override fun getItemCount(): Int = list.size

    public enum class MenuEnum {
        pets, items, service, blogs, profile,subscribe,clinic
    }

    public interface MenuAction {
        fun menuClick(enum: MenuEnum)
    }
}