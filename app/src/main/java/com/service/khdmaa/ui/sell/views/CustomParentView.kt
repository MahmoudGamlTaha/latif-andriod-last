package com.service.khdmaa.ui.sell.views

import android.content.Context
import android.view.View
import android.widget.ArrayAdapter
import com.service.khdmaa.utiles.HintAdapter

abstract class CustomParentView<T>(val context:Context,val label:String,val action :ViewAction<T>?) {
    protected  var view: View? =null
    public  val view_: View?
    get() = view
    var arrayAdapter:HintAdapter? =null
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