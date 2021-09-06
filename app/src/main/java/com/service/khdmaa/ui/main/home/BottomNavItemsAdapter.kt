package com.service.khdmaa.ui.main.home

import android.graphics.ColorSpace
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.util.CollectionUtils
import com.service.khdmaa.R
import com.service.khdmaa.databinding.ItemBottomNavBarBinding
import com.service.khdmaa.databinding.PetItemLayoutBinding
import com.service.khdmaa.databinding.SelectedPetItemBinding
import com.service.khdmaa.databinding.SelecttedItemBottomNavBarBinding
import com.service.khdmaa.ui.main.clinic.ClinicAdapter

class BottomNavItemsAdapter(val action:Action): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val SELECT_ITEM=0
    private val UN_SELECT_ITEM=1
    private var selectedPosition=0
    val list = CollectionUtils.listOf<ClinicAdapter.Model>(
        ClinicAdapter.Model(R.string.MEDICAL, R.drawable.ic_clinic),
        ClinicAdapter.Model(R.string.OCCASIONAL, R.drawable.ic_occasion),
        ClinicAdapter.Model(R.string.service, R.drawable.ic_services),
        ClinicAdapter.Model(R.string.EDUCATIONAL, R.drawable.ic_edu),
        ClinicAdapter.Model(R.string.OTHERS, R.drawable.ic_others)
    )

    class MyViewHolder constructor(val binding: ItemBottomNavBarBinding) :
        RecyclerView.ViewHolder(binding.root)
    class SelectedMyViewHolder constructor(val binding: SelecttedItemBottomNavBarBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType==UN_SELECT_ITEM)
            return MyViewHolder(
                ItemBottomNavBarBinding.inflate(
                    LayoutInflater.from(parent.getContext()),
                    parent, false
                )
            )else return SelectedMyViewHolder(
            SelecttedItemBottomNavBarBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent, false
            )
        )
    }

    override fun getItemViewType(position: Int): Int {
        if (selectedPosition==position)
            return SELECT_ITEM
        else return UN_SELECT_ITEM
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MyViewHolder) {
            holder.itemView.setOnClickListener {
                holder.binding.rootv.apply {
                    action.selectedItem(position)
                    show(position)
                }
            }
            holder.binding.image.setImageResource(list.get(position).image)
        }
        else  if (holder is SelectedMyViewHolder) {

            holder.binding.image.setImageResource(list.get(position).image)
            holder.binding.text.setText(list.get(position).title)
        }
    }

    override fun getItemCount(): Int =list.size

    public fun show(pos:Int){
        selectedPosition=pos
        notifyDataSetChanged()
    }
    interface Action{
        fun selectedItem(pos:Int)
    }
}