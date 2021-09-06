package com.service.khdmaa.ui.sell.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.service.khdmaa.R
import com.service.khdmaa.data.models.AdsTypeModel
import com.service.khdmaa.databinding.SpinnerItemBinding
import kotlinx.android.synthetic.main.spinner_item.view.*

class SpinnerArrayAdapter(var list: List<AdsTypeModel>) : BaseAdapter(), Filterable {

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val binding = SpinnerItemBinding.inflate(
            LayoutInflater.from(parent?.getContext()),
            parent, false
        )

        val view = convertView ?: binding. root
        val text:TextView =view.findViewById(R.id.text1)
        text.text=list.get(position).name
        return view
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(
                constraint: CharSequence,
                results: FilterResults
            ) {
                if (results.count > 0) {
                   // list = results.values as ArrayList<Map<String?, String?>?>
                    notifyDataSetChanged()
                } else {
                    notifyDataSetInvalidated()
                }
            }

            override fun performFiltering(constraint: CharSequence): FilterResults {
                val result = ArrayList<Map<String, String>>()
                val myMap = HashMap<String, String>()
                myMap["name"] = "key"
                result.add(myMap)
                val myMap2 = HashMap<String, String>()
                myMap2["name"] = "is"
                result.add(myMap2)
                val myMap3 = HashMap<String, String>()
                myMap3["name"] = "another"
                result.add(myMap3)
                val r = FilterResults()
                r.values = result
                r.count = result.size
                return r
            }
        }
     }
}