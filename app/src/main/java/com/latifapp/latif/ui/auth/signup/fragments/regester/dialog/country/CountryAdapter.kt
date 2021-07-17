package com.latifapp.latif.ui.auth.signup.fragments.regester.dialog.country

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.latifapp.latif.data.models.CountryModel
import com.latifapp.latif.databinding.CountryItemBinding
import com.latifapp.latif.utiles.Utiles

class CountryAdapter(
    val isEnglish: Boolean, val list: List<CountryModel>,
    val actions: Actions
) :
    RecyclerView.Adapter<CountryAdapter.MyViewHolder>() {
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
        holder.binding.nameTxt.text = if (isEnglish) model.nameEn else model.nameAr
        holder.binding.root.setOnClickListener {
            actions.select(model.id,if (isEnglish) model.nameEn else model.nameAr)
        }
    }

    override fun getItemCount(): Int = list.size

    public interface Actions{
        fun select(id:String,name:String)
    }
}