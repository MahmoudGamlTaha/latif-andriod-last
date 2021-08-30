package com.latifapp.latif.ui.sell.views

import android.content.Context
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import android.widget.Spinner.*
import androidx.appcompat.widget.AppCompatSpinner

import com.latifapp.latif.R
import com.latifapp.latif.data.models.OptionsModel
import com.latifapp.latif.databinding.CustomSpinnerLayoutBinding
import com.latifapp.latif.utiles.HintAdapter


class CustomSpinner(context_: Context, label: String,var list_: List<OptionsModel>,action :ViewAction<String>) :
    CustomParentView<String>(context_, label,action),
      AdapterView.OnItemSelectedListener {

     lateinit var spinnerView: AppCompatSpinner

    override fun createView() {
        Log.d("djdjdjdjjjdjdjd2",label)

        val list:MutableList<String> = list_.map { "${it.label}" } as MutableList<String>
        list.add(context.getString(R.string.choose))
        val spinner= CustomSpinnerLayoutBinding.inflate(LayoutInflater.from(context))
         val arrayAdapter = context?.let {
             HintAdapter(
                it, android.R.layout.simple_list_item_1, list)
        }
        this.arrayAdapter=arrayAdapter

        view=spinner.apply {
            root.visibility= VISIBLE
            label.text=this@CustomSpinner.label
            this@CustomSpinner.spinnerView=adsTypeSpinner
            adsTypeSpinner.setAdapter(arrayAdapter)
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 40, 0, 5)
            root.layoutParams = params
            adsTypeSpinner.onItemSelectedListener=this@CustomSpinner

        }.root
        spinner.adsTypeSpinner.setSelection(list.size-1)
        }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
       try {
           var code:String = ""
           if (position<list_.size) {
               code = list_.get(position).code!!
               action?.getActionId(code)
           }
       }
       catch (e:Exception){
         e.printStackTrace()
       }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
     }


}