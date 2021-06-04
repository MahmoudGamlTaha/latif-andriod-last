package com.latifapp.latif.ui.myAds

import android.view.View
import android.widget.Switch
import com.latifapp.latif.R
import com.latifapp.latif.ui.main.items.PetsListAdapter
import kotlinx.android.synthetic.main.pet_item_view.view.*

class MyAdsAdapter : PetsListAdapter() {
    var action_: MyAdsAction ? = null
    set(value) {
        super.action=value
        field=value
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val switch: Switch = holder.itemView.findViewById(R.id.switchBtn)
        switch.visibility = View.VISIBLE
        val petModel=list.get(position)
        switch.setChecked(petModel.active)
        switch.setOnClickListener {
            action_?.activeAd(!petModel.active,petModel.id,position)
        }

    }

    fun switchActivation(it: Boolean, position: Int) {
        list.get(position).active =it
        notifyItemChanged(position)
    }


    interface MyAdsAction:Action{
        fun activeAd(activeAd:Boolean,id:Int?,position: Int)
    }

}