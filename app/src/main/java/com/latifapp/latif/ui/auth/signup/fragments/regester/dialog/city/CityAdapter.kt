package com.latifapp.latif.ui.auth.signup.fragments.regester.dialog.city

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.latifapp.latif.data.models.CityModel
import com.latifapp.latif.data.models.CountryModel
import com.latifapp.latif.databinding.CountryItemBinding
import com.latifapp.latif.utiles.Utiles

class CityAdapter(
    val isEnglish: Boolean, val list: List<CityModel>,
    val actions: Actions
) :
    RecyclerView.Adapter<CityAdapter.MyViewHolder>() {
    class MyViewHolder(val binding: CountryItemBinding) : RecyclerView.ViewHolder(binding.root) {


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder(
            CountryItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Utiles.log_D("nnnfnnfnfnnfnf",list.size)
        val model = list.get(position)
        holder.binding.nameTxt.text = if (isEnglish) model.cityEn else model.cityAr
        holder.binding.root.setOnClickListener {
            actions.select(model.id, if (isEnglish) "${model.cityEn}" else "${model.cityAr}")
        }
    }

    override fun getItemCount(): Int = list.size

    public interface Actions{
        fun select(id:String,name:String)
    }
}