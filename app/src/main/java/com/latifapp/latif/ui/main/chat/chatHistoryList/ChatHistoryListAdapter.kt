package com.latifapp.latif.ui.main.chat.chatHistoryList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.latifapp.latif.R
import com.latifapp.latif.data.models.MsgNotification
import com.latifapp.latif.databinding.ChatHistoryItemLayoutBinding
import com.latifapp.latif.databinding.PetImageItemBinding
import com.latifapp.latif.ui.auth.signup.fragments.interests.InterestsAdapter
import com.latifapp.latif.ui.details.PetImageAdapter

class ChatHistoryListAdapter(val action:Action) : RecyclerView.Adapter<ChatHistoryListAdapter.MyViewHolder>() {

    val list = mutableListOf<MsgNotification>()
    class MyViewHolder(val binding:ChatHistoryItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ChatHistoryItemLayoutBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model=list.get(position)
        holder.binding.nameTxt.text="${model.sender_name}"
        holder.binding.msg.text="${model.message}"
        holder.binding.dateTxt.text="${model.createAt}"
        if ( ! model.sender_avater.isNullOrEmpty()){
            Glide.with(holder.itemView.context).load(model.sender_avater)
                .error(R.drawable.ic_person)
                .placeholder(R.drawable.ic_person).into(holder.binding.image)
        }
        holder.itemView.setOnClickListener {
            action.onItemClick(model)
        }
    }

    override fun getItemCount(): Int =list.size

    interface Action{
        fun onItemClick(model:MsgNotification)
    }
}