package com.service.khdmaa.ui.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.util.CollectionUtils
import com.service.khdmaa.R
import com.service.khdmaa.databinding.MenuItemBinding

class MenuAdapter(val action: MenuAction) : RecyclerView.Adapter<MenuAdapter.MyViewHolder>() {
    val list = CollectionUtils.listOf(
        R.string.MEDICAL, R.string.OCCASIONAL,
        R.string.commerical,
        R.string.service,
        R.string.OTHERS,
        R.string.subscribe,
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
            var anEnum = MenuEnum.medical
            when (position) {
                0 -> anEnum = MenuEnum.medical
                1 -> anEnum = MenuEnum.occasion
                2 -> anEnum = MenuEnum.commerical
                3 -> anEnum = MenuEnum.service
                4 -> anEnum = MenuEnum.others
                5 -> anEnum = MenuEnum.subscribe
                6 -> anEnum = MenuEnum.profile
            }
            action.menuClick(anEnum)
        }
    }

    override fun getItemCount(): Int = list.size

    public enum class MenuEnum {
        medical, occasion, service, education, profile,subscribe,commerical,others
    }

    public interface MenuAction {
        fun menuClick(enum: MenuEnum)
    }
}