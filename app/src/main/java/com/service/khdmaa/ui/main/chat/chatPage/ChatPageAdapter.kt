package com.service.khdmaa.ui.main.chat.chatPage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.service.khdmaa.data.models.ChatResponseModel
import com.service.khdmaa.databinding.ChatRecieveItemBinding
import com.service.khdmaa.databinding.ChatSendItemBinding
import com.service.khdmaa.databinding.PetItemLayoutBinding
import com.service.khdmaa.databinding.SelectedPetItemBinding

class ChatPageAdapter(val userId:String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val SEND_ITEM=0
    private val Recieve_ITEM=1

    val list = mutableListOf<ChatResponseModel>()

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
        if (!list.get(position).senderId.equals("$userId"))
            return Recieve_ITEM
        else return SEND_ITEM
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model=list.get(position)
        if (holder is RecieveViewHolder  ){
            holder.binding.text.text=model.message
        }else if (holder is SendViewHolder  ){
            holder.binding.text.text=model.message
        }
    }

    override fun getItemCount(): Int =list.size
    fun addComment(msg: ChatResponseModel) {
        list.add(msg)
        notifyItemInserted(list.size-1)
    }
}