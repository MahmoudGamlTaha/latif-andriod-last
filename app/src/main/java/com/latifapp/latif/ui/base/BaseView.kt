package com.latifapp.latif.ui.base

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import com.latifapp.latif.R
import com.latifapp.latif.utiles.Utiles

open interface BaseView<B: ViewBinding> {

     fun setBindingView(inflater: LayoutInflater): B
     fun showLoader()
     fun hideLoader()

     fun toastMsg_Warning(msg: String, view: View, context:Context) {
        try {
            val snackbar = Snackbar.make(
                view, msg,
                Snackbar.LENGTH_LONG
            ).setAction("Action", null)
            snackbar.setActionTextColor(Color.BLUE)
            val snackbarView = snackbar.view
            snackbarView.setBackgroundResource(R.drawable.edittext_bg)
            val textView =
                snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
            textView.setTextColor(ContextCompat.getColor(context, R.color.dark_red))
            textView.textSize = 15f
            textView.gravity = Gravity.CENTER
            textView.maxLines = 1
            textView.compoundDrawablePadding= 20
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_info, 0, 0, 0)
            snackbar.show()
        } catch (e: Exception) {
        }
    }
     fun toastMsg_Success(msg: String, view: View, context:Context) {
        try {
            val snackbar = Snackbar.make(
                view, msg,
                Snackbar.LENGTH_LONG
            ).setAction("Action", null)
            snackbar.setActionTextColor(Color.BLUE)
            val snackbarView = snackbar.view
            snackbarView.setBackgroundResource(R.drawable.green_bg)
            val textView =
                snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
            textView.setTextColor(ContextCompat.getColor(context, R.color.white))
            textView.textSize = 15f
            textView.gravity = Gravity.CENTER
            textView.maxLines = 1
            textView.compoundDrawablePadding= 20
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_success, 0, 0, 0)
            snackbar.show()
        } catch (e: Exception) {
            Utiles.log_D("Exception",e)
        }
    }
}