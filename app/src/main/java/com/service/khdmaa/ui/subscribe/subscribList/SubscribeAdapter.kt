package com.service.khdmaa.ui.subscribe.subscribList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.service.khdmaa.data.models.SubscribeModel
import com.service.khdmaa.databinding.SubscribItemBinding
import com.service.khdmaa.ui.main.items.PetsListAdapter


class SubscribeAdapter(val isEnglish:Boolean) : RecyclerView.Adapter<SubscribeAdapter.MyViewHolder>() {
    var action: SubscribeAction? =null
     var list = mutableListOf<SubscribeModel>()
    set(value) {
        field.addAll(value)
        notifyDataSetChanged()
    }

    class MyViewHolder(val binding: SubscribItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            SubscribItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.text.text = if (isEnglish)list.get(position).name else list.get(position).nameAr
        holder.itemView.setOnClickListener { action?.subscribeClick("${list.get(position).id}") }
    }

    override fun getItemCount(): Int = list.size
    interface SubscribeAction {
        fun subscribeClick(id:String)
    }
}