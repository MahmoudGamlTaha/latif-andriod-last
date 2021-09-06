package com.service.khdmaa.ui.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.service.khdmaa.data.models.ExtraModel
import com.service.khdmaa.databinding.ExtraItemBinding

public class ExtraAdapter(val extra: List<ExtraModel>, val isEnglish: Boolean) :
    RecyclerView.Adapter<ExtraAdapter.MyViewHolder>() {
    class MyViewHolder (val binding: ExtraItemBinding) : RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ExtraItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = extra.get(position)
        var name="${model.name}"
        if (!isEnglish)
         name="${model.name_ar}"
        holder.binding.nameTxt.text = "$name:"
        if (model.value.toString().isBlank() || model.value.toString() == "null") {
            model.value = "N/A"
            model.value_ar = "N/A"
        }
        if ( model.value.toString() == "false") {
            model.value = "No"
            model.value_ar = "لا"
        }
        if ( model.value.toString() == "true") {
            model.value = "Yes"
            model.value_ar = "نعم"
        }

        holder.binding.valueTxt.text = "${if (isEnglish) model.value else model.value_ar}"
    }

    override fun getItemCount(): Int {
        return extra.size
    }
}