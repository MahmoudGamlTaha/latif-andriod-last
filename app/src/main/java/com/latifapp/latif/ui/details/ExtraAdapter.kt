package com.latifapp.latif.ui.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.latifapp.latif.data.models.ExtraModel
import com.latifapp.latif.databinding.ExtraItemBinding

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
        }
        if ( model.value.toString() == "false") {
            model.value = "No"
        }
        if ( model.value.toString() == "true") {
            model.value = "Yes"
        }

        holder.binding.valueTxt.text = "${model.value}"
    }

    override fun getItemCount(): Int {
        return extra.size
    }
}