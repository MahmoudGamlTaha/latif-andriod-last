package com.service.khdmaa.ui.sell.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.service.khdmaa.data.models.OptionsModel
import com.service.khdmaa.databinding.CheckboxLayoutBinding
import com.service.khdmaa.databinding.RadiobuttonLayoutBinding

class RadioButtonAdapter (val list:List<OptionsModel>,val action: CheckBoxsListAdapter.CheckBoxAction) : RecyclerView.Adapter<RadioButtonAdapter.MyViewHolder>() {
    private var selectedcheckbox:RadioButton?=null
     class MyViewHolder (val binding: RadiobuttonLayoutBinding) : RecyclerView.ViewHolder(binding.root){
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            RadiobuttonLayoutBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent, false
            ))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.checkbox.text=list.get(position).label

        holder.binding.checkbox.setOnCheckedChangeListener{ compoundButton: CompoundButton, b: Boolean ->
            if (b) {
                action.getChecked(list.get(position).code!!)
                if (selectedcheckbox!=null)
                    selectedcheckbox?.isChecked=false
                selectedcheckbox=holder.binding.checkbox

            }
        }
    }

    override fun getItemCount(): Int=list.size

}