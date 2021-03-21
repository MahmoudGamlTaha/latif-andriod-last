package com.latifapp.latif.ui.main.chat.chatPage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.latifapp.latif.databinding.ChatRecieveItemBinding
import com.latifapp.latif.databinding.ChatSendItemBinding
import com.latifapp.latif.databinding.PetItemLayoutBinding
import com.latifapp.latif.databinding.SelectedPetItemBinding

class ChatPageAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val SEND_ITEM=0
    private val Recieve_ITEM=1

    val list = mutableListOf(
        "hi sara", "what's up", "hi ..", "iam ok",
        "what's up"
    )

    class SendViewHolder constructor(val binding: ChatSendItemBinding) :
        RecyclerView.ViewHolder(binding.root)
    class RecieveViewHolder constructor(val binding: ChatRecieveItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType==Recieve_ITEM)
            return RecieveViewHolder(
                ChatRecieveItemBinding.inflate(
                    LayoutInflater.from(parent.getContext()),
                    parent, false
                )
            )else return SendViewHolder(
            ChatSendItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent, false
            )
        )
    }

    override fun getItemViewType(position: Int): Int {
        if (position==0||position==1)
            return Recieve_ITEM
        else return SEND_ITEM
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is RecieveViewHolder  ){
            holder.binding.text.text=list.get(position)
        }else if (holder is SendViewHolder  ){
            holder.binding.text.text=list.get(position)
        }
    }

    override fun getItemCount(): Int =list.size
    fun addComment(msg: String) {
        list.add(msg)
        notifyItemInserted(list.size-1)
    }
}