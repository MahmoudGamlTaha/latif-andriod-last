package com.service.khdmaa.ui.main.items

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.service.khdmaa.R
import com.service.khdmaa.data.models.AdsModel
import com.service.khdmaa.databinding.PetItemViewBinding
import com.service.khdmaa.utiles.Utiles


open class PetsListAdapter : RecyclerView.Adapter<PetsListAdapter.MyViewHolder>() {
    open var action: Action? = null
    var list: MutableList<AdsModel> = arrayListOf()
        set(value) {
            field.addAll(value)
            notifyDataSetChanged()
        }

    class MyViewHolder(val binding: PetItemViewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            PetItemViewBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = list.get(position)
        holder.binding.petName.text = "${model.name}"
        holder.binding.dateTxt.text = "${model.created_at}"
        holder.binding.priceTxt.text = "${model.price} EGP"
        var length = "${model.short_description}"?.length - 1
        if(length > 30){
            length = 30
        }
        Utiles.log_D("length short", length)
        holder.binding.locTxt.text = "${model.short_description}"?.substring(0, length)+"..."
        holder.binding.cityTxt.text="${model.city}"

        var ads_type: String? =null

        val itr = model?.extra?.iterator()
        // or, use `iterator()`
        itr?.apply {
            while (hasNext()) {
                val extr=   next()
                if(extr.name.equals("Ad Type")){
                    ads_type= extr.value as? String
                    break;
                }
            }
        }

        if(ads_type.isNullOrBlank()) {
            holder.binding.adBadgeBtn.visibility = GONE
        }
        else{
            holder.binding.adBadgeBtn.setText(ads_type)
            if(ads_type.equals("Selling",true)){
                holder.binding.adBadgeBtn.setBackgroundColor(Color.parseColor("#7587ff"))
            }
            else if(ads_type.equals("Adopt",true)){
                holder.binding.adBadgeBtn.setBackgroundColor(Color.parseColor("#0ab687"))
            }
           else if(ads_type.equals("Mating",true)){
                holder.binding.adBadgeBtn.setBackgroundColor(Color.parseColor("#9821aa"))
            }
            else{

                holder.binding.adBadgeBtn.setBackgroundColor(Color.parseColor("#ff5551"))

            }
        }


        var image = model.image

        if (!image.isNullOrEmpty())
            Glide.with(holder.itemView.context).load(image)
                .error(R.drawable.ic_defaultpet)
                .placeholder(R.drawable.ic_defaultpet).into(holder.binding.image)
        else holder.binding.image.setImageResource(R.drawable.ic_defaultpet)
        holder.itemView.setOnClickListener {
            action?.onAdClick(model.id)
        }

    }

    override fun getItemCount(): Int = list.size

    interface Action {
        fun onAdClick(id: Int?)
    }
}