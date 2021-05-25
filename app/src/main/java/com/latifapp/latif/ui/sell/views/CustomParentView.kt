package com.latifapp.latif.ui.sell.views

import android.content.Context
import android.view.View
import android.widget.ArrayAdapter

abstract class CustomParentView<T>(val context:Context,val label:String,val action :ViewAction<T>?) {
    protected  var view: View? =null
    var arrayAdapter:ArrayAdapter<String>? =null
    init {

    }
    abstract fun createView()

    @JvmName("getView1")
    fun getView(): View? {
        createView()
         return view
    }

    public interface ViewAction<T>{
        fun getActionId(text:T)
    }
}